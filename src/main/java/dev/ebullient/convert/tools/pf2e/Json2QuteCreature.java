package dev.ebullient.convert.tools.pf2e;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.ebullient.convert.tools.JsonNodeReader;
import dev.ebullient.convert.tools.Tags;
import dev.ebullient.convert.tools.pf2e.qute.*;

public class Json2QuteCreature extends Json2QuteBase {

    public Json2QuteCreature(Pf2eIndex index, JsonNode rootNode) {
        super(index, Pf2eIndexType.creature, rootNode);
    }

    @Override
    protected QuteCreature buildQuteResource() {
        List<String> text = new ArrayList<>();
        Tags tags = new Tags(sources);

        appendToText(text, SourceField.entries.getFrom(rootNode), "##");

        Collection<String> traits = collectTraitsFrom(rootNode, tags);
        if (Pf2eCreature.alignment.existsIn(rootNode)) {
            traits.addAll(toAlignments(rootNode, Pf2eCreature.alignment));
        }
        Optional<Integer> level = Pf2eCreature.level.getIntFrom(rootNode);
        level.ifPresent(integer -> tags.add("creature", "level", integer.toString()));
        Collection<QuteInlineAttack> attacks = buildAttacks();

        return new QuteCreature(sources,
            text,
            tags,
            traits,
            Field.alias.replaceTextFromList(rootNode, this),
            Pf2eCreature.description.replaceTextFrom(rootNode, this),
            level.orElse(null),
            buildPerception().get(),
            buildSenses(),
            buildLanguages(),
            buildAbilityMods(),
            buildSkills(),
            buildItems(),
            buildSpeed(),
            buildAttacks(),
            buildSpellcasting(),
            buildAbilities("top"),
            buildAbilities("mid"),
            buildAbilities("bot"),
            buildDefenses());
    }

    private String buildLanguages() {
        JsonNode languageNode = Pf2eCreature.languages.getFrom(rootNode);
        if (languageNode == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        if(languageNode.hasNonNull("languages")) {
            languageNode.get("languages").forEach(l -> sb.append(toTitleCase(l.asText())).append(", "));
        }
        if (languageNode.hasNonNull("notes")) {
            languageNode.get("notes").forEach(noteNode -> {
                sb.append(" (");
                sb.append(replaceText(noteNode.asText()));
                sb.append(")");
            });
        }
        if (languageNode.hasNonNull("abilities")) {
            languageNode.get("abilities").forEach(abNote -> {
                sb.append(" (");
                sb.append(replaceText(abNote.asText()));
                sb.append(")");
            });
        }
        return sb.toString();
    }

    private Collection<QuteDataSpellcasting> buildSpellcasting() {
        List<QuteDataSpellcasting> spellcastings = new ArrayList<>();
        Pf2eCreature.spellcasting.getFrom(rootNode);

        JsonNode array = Pf2eCreature.spellcasting.getFrom(rootNode);
        if (array == null || array.isNull()) {
            return null;
        } else if (array.isObject()) {
            tui().errorf("Unknown spellcasting for %s: %s", sources.getKey(), array.toPrettyString());
            throw new IllegalArgumentException("Unknown spellcasting: " + getSources());
        }
        array.forEach(node -> {
            QuteDataSpellcasting spellcasting = new QuteDataSpellcasting();
            spellcasting.name = SourceField.name.replaceTextFrom(node, this);
            if (node.has("tradition")) {
                spellcasting.tradition = toTitleCase(node.get("tradition").asText());
            }
            spellcasting.type = node.get("type").asText(null);
            if (node.has("DC")) {
                spellcasting.DC = node.get("DC").asInt();
            }
            if (node.has("fp")) {
                spellcasting.FP = node.get("fp").asInt();
            }
            spellcasting.spells = new TreeMap<>();
            node.get("entry").fields().forEachRemaining(
                f -> {
                    if (f.getKey().equals("constant")) {
                        f.getValue().fields().forEachRemaining(
                            c -> spellcasting.spells.put("Constant" + "(" + numToSpellLevel(f) + ")", getSpells(c.getValue().get("spells"))));
                    } else {
                        spellcasting.spells.put(numToSpellLevel(f), getSpells(f.getValue().get("spells")));
                    }
                });

            spellcastings.add(spellcasting);
        });
        return spellcastings;
    }

    private String numToSpellLevel(Map.Entry<String, JsonNode> map) {
        return switch (map.getKey()) {
            case "0" -> String.format("Cantrips (%s)", map.getValue().get("level").asText());
            default -> getOrdinalForm(map.getKey());
        };
    }

    private List<String> getSpells(JsonNode node) {
        if (node == null) {
            tui().errorf("Null spells from %s", sources.getKey());
            return List.of();
        }
        List<String> spells = new ArrayList<>();
        node.forEach(s -> {
            String spellName = linkify(Pf2eIndexType.spell, s.get("name").asText());
            if (s.has("amount")) {
                spellName += " (" + s.get("amount").asText() + ");";
            }
            if (s.has("notes")) {
                List<String> notes = new ArrayList<>();
                s.get("notes").forEach(
                    note -> notes.add(note.asText())
                );
                spellName += String.join(",", notes) + ";";
            }
            spells.add(spellName);
        });
        return spells;
    }

    private String buildSpeed() {
        Speed speed = Json2QuteCreature.Pf2eCreature.speed.fieldFromTo(rootNode, Speed.class, tui());
        if (speed != null) {
            return speed.speedToString(this);
        }
        return null;
    }

    private Optional<Integer> buildPerception() {
        Optional<Integer> perception = Optional.of(0);
        if (Pf2eCreature.perception.getFieldFrom(rootNode, () -> "std") != null) {
            perception = Optional.of(Pf2eCreature.perception.getFieldFrom(rootNode, () -> "std").asInt());
        }
        return perception;
    }

    private Collection<String> buildItems() {
        List<String> items = new ArrayList<>();
        Json2QuteCreature.Pf2eCreature.items.getListOfStrings(rootNode, tui()).forEach(x -> items.add(replaceText(x)));
        return items;
    }

    private Collection<QuteDataSenses> buildSenses() {
        Collection<QuteDataSenses> senses = new ArrayList<>();
        Pf2eCreature.senses.withArrayFrom(rootNode).forEach(a -> senses.add(new QuteDataSenses(a.path("name").asText(), linkify(Pf2eIndexType.ability, a.path("type").asText(null)), a.path("range").asText(null))));
        return senses;
    }

    private Collection<QuteInlineAttack> buildAttacks() {
        List<QuteInlineAttack> attacks = new ArrayList<>();
        Pf2eCreature.attacks.withArrayFrom(rootNode).forEach(a -> attacks.add(AttackField.createInlineAttack(a, this)));
        return attacks;
    }

    private List<QuteAbility> buildAbilities(String region) {
        JsonNode node = Pf2eCreature.abilities.getFrom(rootNode);
        List<QuteAbility> abilities = new ArrayList<>();
        if (node != null && node.has(region)) {
            node.get(region).forEach(x -> abilities.add(Pf2eTypeAbility.createAbility(x, this, false)));
        }
        return abilities;

    }

    private QuteDataAbilityMods buildAbilityMods() {
        Map<String, Integer> aM = new HashMap<>();
        if (Pf2eCreature.abilityMods.existsIn(rootNode)) {
            aM = Pf2eCreature.abilityMods.fieldFromTo(rootNode, HashMap.class, tui());
        }
        QuteDataAbilityMods abilityMods = new QuteDataAbilityMods();
        abilityMods.setStrength(aM.getOrDefault("str", 0))
            .setConstitution(aM.getOrDefault("con", 0))
            .setDexterity(aM.getOrDefault("dex", 0))
            .setIntelligence(aM.getOrDefault("int", 0))
            .setWisdom(aM.getOrDefault("wis", 0))
            .setCharisma(aM.getOrDefault("cha", 0));
        return abilityMods;
    }

    private QuteDataSkills buildSkills() {
        Map<String, Map<String, String>> aM = new HashMap<>();
        if (Pf2eCreature.skills.existsIn(rootNode)) {
            aM = Json2QuteCreature.Pf2eCreature.skills.fieldFromTo(rootNode, Map.class, tui());
        }
        List<String> skillList = List.of("arcana", "athletics", "acrobatics", "crafting", "diplomacy", "survival", "intimidation", "deception", "lore", "medicine", "nature", "occultism", "religion", "performance", "society", "stealth", "thievery");
        if (!skillList.containsAll(aM.keySet())) {
            Set<String> missingKeys = aM.keySet();
            skillList.forEach(missingKeys::remove);
            missingKeys.forEach(s -> tui().debugf("TODO: Unsupported Skill: %s", s));

        }
        QuteDataSkills skills = new QuteDataSkills();
        skills.setArcana(aM.get("arcana"))
            .setAthletics(aM.get("athletics"))
            .setAcrobatics(aM.get("acrobatics"))
            .setCrafting(aM.get("crafting"))
            .setDiplomacy(aM.get("diplomacy"))
            .setIntimidation(aM.get("intimidation"))
            .setDeception(aM.get("deception"))
            .setLore(aM.get("lore"))
            .setMedicine(aM.get("medicine"))
            .setNature(aM.get("nature"))
            .setOccultism(aM.get("occultism"))
            .setReligion(aM.get("religion"))
            .setPerformance(aM.get("performance"))
            .setSociety(aM.get("society"))
            .setStealth(aM.get("stealth"))
            .setSurvival(aM.get("survival"))
            .setThievery(aM.get("thievery"));
        return skills;
    }

    private QuteDataDefenses buildDefenses() {
        JsonNode defenseNode = Json2QuteHazard.Pf2eHazard.defenses.getFrom(rootNode);
        if (defenseNode == null) {
            return null;
        }
        return Pf2eDefenses.createInlineDefenses(defenseNode, this);
    }

    enum Pf2eCreature implements JsonNodeReader {
        abilities, abilityMods, alignment, attacks, defenses, description, hasImages, inflicts, isNpc, items, languages, level, perception, rarity, rituals, senses, size, skills, speed, spellcasting, traits,
    }
}
