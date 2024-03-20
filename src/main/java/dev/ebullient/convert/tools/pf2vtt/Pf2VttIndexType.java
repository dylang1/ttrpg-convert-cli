package dev.ebullient.convert.tools.pf2vtt;

import com.fasterxml.jackson.databind.JsonNode;
import dev.ebullient.convert.tools.IndexType;
import dev.ebullient.convert.tools.JsonNodeReader;
import dev.ebullient.convert.tools.JsonTextConverter;
import dev.ebullient.convert.tools.pf2vtt.qute.Pf2VttQuteBase;
import dev.ebullient.convert.tools.pf2vtt.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Pf2VttIndexType implements IndexType, JsonNodeReader {

    ///NEEDS TIDIED UP TO MATCH THE FOUNDRY JSON
    ability, // B1
    action(List.of("actionspf2e")),
    adventure,
    affliction,
    ancestry,
    archetype,
    background,
    book,
    classFeature(List.of("classfeatures")),
    classtype("class"),
    companion,
    companionAbility,
    condition(List.of("conditionitems")),
    creature, // B1
    creatureTemplate, // B1
    curse("affliction"), // GMG
    data, // data from any source
    deity,
    disease("affliction"), // GMG
    domain,
    eidolon, // SoM
    event, // LOTG
    familiar, // APG
    familiarAbility,
    feat(List.of("feats-srd")),
    group,
    hazard,
    baseitem,
    item(List.of("equipment-srd")),
    language,
    nation, // GMG
    optfeature, // APG
    organization, // LOCG
    place, // GMG
    plane, // GMG
    relicGift, // GMG
    ritual,
    settlement, // GMG
    skill,
    spell(List.of("spells-srd")),
    subclassFeature,
    table,
    trait,
    trap,
    variantrule, // GMG
    vehicle, // GMG
    versatileHeritage, // APG
    syntheticGroup, // for this tool only
    bookReference, // this tool only
    journal(List.of("journals")),
    ignore(List.of("pf2e-macros","feat-effects","other-effects","equipment-effects"))
    ;

    final String templateName;
    final List<String> links;



    Pf2VttIndexType() {
        this.templateName = this.name();
        this.links = new ArrayList<>();
    }

    Pf2VttIndexType(String templateName) {
        this.templateName = templateName;
        this.links = new ArrayList<>();
    }
    Pf2VttIndexType(String templateName,List<String> links){
        this.templateName = templateName;
        this.links = links;
    }
    Pf2VttIndexType(List<String> links){
        this.templateName = this.name();
        this.links = links;
    }

    public String templateName() {
        return templateName;
    }


    public static final Pattern matchPattern = Pattern.compile("\\{@("
        + Stream.of(values())
        .flatMap(x -> Stream.of(x.templateName, x.name()))
        .distinct()
        .collect(Collectors.joining("|"))
        + ") ([^{}]+?)}");

    public static final Pattern linkPattern = Pattern.compile("@UUID\\[(.+?)\\]");


    public boolean isDefaultSource(String source) {
        return defaultSource().sameSource(source);
    }

    public void withArrayFrom(JsonNode node, BiConsumer<Pf2VttIndexType, JsonNode> callback) {
        node.withArray(this.nodeName()).forEach(x -> callback.accept(this, x));
    }

    public static Pf2VttIndexType fromText(String name) {
        return Stream.of(values())
            .filter(x -> x.templateName.equals(name) || x.name().equalsIgnoreCase(name) || x.links.contains(name))
            .findFirst().orElse(null);
    }

    public static Pf2VttIndexType getTypeFromKey(String key) {
        String typeKey = key.substring(0, key.indexOf("|"));
        return valueOf(typeKey);
    }

    public String createKey(JsonNode node) {
        String name = JsonTextConverter.SourceField.name.getTextOrEmpty(node);
        //TODO: Fix this to use the enum nodes and possibly change title to be an abbrev of the actual book
        String source = node.get("system").get("publication").get("title").asText(this.defaultSourceString());

//
//
//        if (this == book || this == adventure) {
//            String id = JsonTextConverter.SourceField.id.getTextOrEmpty(node);
//            return String.format("%s|%s-%s", this.name(), this.name(), id).toLowerCase();
//        }
//
//        String name = JsonTextConverter.SourceField.name.getTextOrEmpty(node);
//        String source = JsonTextConverter.SourceField.source.getTextOrDefault(node, this.defaultSourceString());
        return String.format("%s|%s|%s", this.name(), name, source).toLowerCase();
    }

    public String createKey(String name, String source) {
        if (source == null || this == data) {
            return String.format("%s|%s", this.name(), name).toLowerCase();
        }
        return String.format("%s|%s|%s", this.name(), name, source).toLowerCase();
    }

    public String getVaultRoot(Pf2VttIndex index) {
        return useCompendiumBase() ? index.compendiumVaultRoot() : index.rulesVaultRoot();
    }

    public Path getFilePath(Pf2VttIndex index) {
        return useCompendiumBase() ? index.compendiumFilePath() : index.rulesFilePath();
    }

    public String relativeRepositoryRoot(Pf2VttIndex index) {
        String root = getVaultRoot(index);
        String relativePath = relativePath();

        if (relativePath.isEmpty() || ".".equals(relativePath)) {
            return root.replaceAll("/$", "");
        }
        return root + relativePath;
    }

    public Pf2VttQuteBase convertJson2QuteBase(Pf2VttIndex index, JsonNode node) {
        Pf2VttIndexType type = this;
        switch (this) {
            // Group: Affliction/Curse/Disease
//            case affliction:
//                type = Pf2eIndexType.fromText(JsonTextConverter.SourceField.type.getTextOrDefault(node, "Disease"));
//            case curse:
//            case disease:
//                return new Json2QuteAffliction(index, type, node).build();
//            // Other type
            case action:
                return new Json2QuteAction(index, node).build();
//            case archetype:
//                return new Json2QuteArchetype(index, node).build();
//            case background:
//                return new Json2QuteBackground(index, node).build();
//            case deity:
//                return new Json2QuteDeity(index, node).build();
//            case feat:
//                return new Json2QuteFeat(index, node).build();
//            case hazard:
//                return new Json2QuteHazard(index, node).build();
//            case item:
//                return new Json2QuteItem(index, node).build();
//            case ritual:
//                return new Json2QuteRitual(index, node).build();
//            case spell:
//                return new Json2QuteSpell(index, node).build();
//            case trait:
//                return new Json2QuteTrait(index, node).build();
//            case creature:
//                return new Json2QuteCreature(index,node).build();
            default:
                return null;
        }
    }

    public boolean alwaysInclude() {
        return false;
//            switch (this) {
//            case bookReference, data, syntheticGroup -> true;
//            default -> false;
//        };
    }

    public boolean checkCopiesAndReprints() {
        return false;
//            switch (this) {
//            case adventure, book, data, syntheticGroup -> false; // don't check copy/reprint fields
//            default -> true;
//        };
    }

    public boolean useQuteNote() {
        return false;
//            switch (this) {
//            case ability, condition, domain, skill, table -> true; // QuteNote-based
//            default -> false;
//        };
    }

    public boolean useCompendiumBase() {
        return switch (this) {
            case
//                ability,
                action
//                    , book, condition, trait, table, variantrule
                    -> false; // use rules
            default -> true; // use compendium
        };
    }

    public String relativePath() {
        return switch (this) {
            // Simple suffix subdir (rules or compendium)
            case action
//                feat, spell, table, trait, variantrule
                -> this.name() + 's';
//            case ritual -> "spells/rituals";
//            // Character
//            case ancestry -> "character/ancestries";
//            case classtype -> "character/classes";
//            case archetype, background, companion -> "character/" + this.name() + 's';
//            // Equipment
//            case item, vehicle -> "equipment/" + this.name() + 's';
//            // GM
//            case curse, disease -> "gm/afflictions";
//            case creature, hazard -> "gm/" + this.name() + 's';
//            case relicGift -> "gm/relics-gifts";
//            // Setting
//            case domain -> "setting";
//            case adventure, language, organization, place, plane, event -> "setting/" + this.name() + 's';
//            case deity -> "setting/deities";
//            case ability -> "abilities";
            default -> ".";
        };
    }

    public String defaultSourceString() {
        return defaultSource().name();
    }

    public Pf2VttSources.DefaultSource defaultSource() {
        return switch (this) {
//            case familiar, optfeature, versatileHeritage -> Pf2eSources.DefaultSource.apg;
//            case ability, creature, creatureTemplate -> Pf2eSources.DefaultSource.b1;
            case action ->
//                , adventure, ancestry, archetype, background, book, classFeature, classtype, companion, companionAbility,
//                condition, deity, domain, familiarAbility, feat, group, hazard, item, language, ritual, skill, spell,
//                subclassFeature, table, trait, trap, bookReference ->
                Pf2VttSources.DefaultSource.crb;
//            case affliction, curse, disease, nation, place, plane, relicGift, settlement, variantrule, vehicle ->
//                Pf2eSources.DefaultSource.gmg;
//            case organization -> Pf2eSources.DefaultSource.locg;
//            case event -> Pf2eSources.DefaultSource.lotg;
//            case eidolon -> Pf2eSources.DefaultSource.som;
            default -> {
//                System.out.println("Using default as switch is missing " + this);
                yield Pf2VttSources.DefaultSource.crb;
            }
        };
        //                throw new IllegalStateException("How did we get here? Switch is missing " + this);

    }

}
