package dev.ebullient.convert.tools.pf2e.qute;

import dev.ebullient.convert.tools.Tags;
import dev.ebullient.convert.tools.pf2e.Pf2eSources;
import io.quarkus.qute.TemplateData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Pf2eTools Creature attributes ({@code creature2md.txt})
 * <p>
 * Use `%%--` to mark the end of the preamble (frontmatter and
 * other leading content only appropriate to the standalone case).
 * </p>
 * <p>
 * Extension of {@link dev.ebullient.convert.tools.pf2e.qute.Pf2eQuteBase Pf2eQuteBase}
 * </p>
 */
@TemplateData
public class QuteCreature extends Pf2eQuteBase {

    /**
     * Aliases for this note (optional)
     */
    public final List<String> aliases;
    /**
     * Collection of traits (decorated links, optional)
     */
    public final Collection<String> traits;
    /**
     * Short creature description (optional)
     */
    public final String description;
    /**
     * Creature level (number, optional)
     */
    public final Integer level;
    public final Integer perception;
    public final Collection<QuteDataSenses> senses;
    public final String languages;
    public final QuteDataAbilityMods abilityMods;
    public final QuteDataSkills skills;
    public final Collection<String> items;
    public final String speed;
    public final Collection<QuteInlineAttack> attacks;
    public final Collection<QuteDataSpellcasting> spellcastings;
    public final Collection<QuteAbility> topAbilities;
    public final Collection<QuteAbility> midAbilities;
    public final Collection<QuteAbility> botAbilities;
    public final QuteDataDefenses defenses;

    public QuteCreature(Pf2eSources sources, List<String> text, Tags tags, Collection<String> traits, List<String> aliases, String description, Integer level, Integer perception, Collection<QuteDataSenses> senses, String languages, QuteDataAbilityMods abilityMods, QuteDataSkills skills, Collection<String> items, String speed, Collection<QuteInlineAttack> attacks, Collection<QuteDataSpellcasting> spellcastings, Collection<QuteAbility> topAbilities, Collection<QuteAbility> midAbilities, Collection<QuteAbility> botAbilities, QuteDataDefenses defenses) {
        super(sources, text, tags);
        this.aliases = aliases;
        this.traits = traits;
        this.description = description;
        this.level = level;
        this.perception = perception;
        this.senses = senses;
        this.languages = languages;
        this.abilityMods = abilityMods;
        this.skills = skills;
        this.items = items;
        this.speed = speed;
        this.attacks = attacks;
        this.spellcastings = spellcastings;
        this.topAbilities = topAbilities;
        this.midAbilities = midAbilities;
        this.botAbilities = botAbilities;
        this.defenses = defenses;
    }

//    public QuteCreature(Pf2eSources sources, String name, String source, String text, Tags tags, List<String> aliases, Collection<String> traits, String description, Integer level, Integer perception, Collection<QuteDataSenses> senses, String languages, QuteDataAbilityMods abilityMods, QuteDataSkills skills, Collection<String> items, String speed, Collection<QuteInlineAttack> attacks, Collection<QuteDataSpellcasting> spellcastings, Collection<QuteAbility> topAbilities, Collection<QuteAbility> midAbilities, Collection<QuteAbility> botAbilities, QuteDataDefenses defenses) {
//        super(sources, name, source, text, tags);
//        this.aliases = aliases;
//        this.traits = traits;
//        this.description = description;
//        this.level = level;
//        this.perception = perception;
//        this.senses = senses;
//        this.languages = languages;
//        this.abilityMods = abilityMods;
//        this.skills = skills;
//        this.items = items;
//        this.speed = speed;
//        this.attacks = attacks;
//        this.spellcastings = spellcastings;
//        this.topAbilities = topAbilities;
//        this.midAbilities = midAbilities;
//        this.botAbilities = botAbilities;
//        this.defenses = defenses;
//    }

    public Collection<String> attacks(){
        List<String> attackString = new ArrayList<>();
        for (QuteInlineAttack attack: attacks ) {
            String sb = " ***" +
                attack.meleeOrRanged +
                "*** " +
                attack.activity +
                attack.getName() +
                attack.traits +
                "**Damage** " +
                attack.damage;
            attackString.add(sb);

        }
        return attackString;
    }
    public Collection<String> topAbilities(){
        return getAbilities(topAbilities);
    }
    public Collection<String> midAbilities(){
        return getAbilities(midAbilities);
    }
    public Collection<String> botAbilities(){
        return getAbilities(botAbilities);
    }
    public String defenses(){
        List<String> lines = new ArrayList<>();
        List<String> first = new ArrayList<>();
        List<String> second = new ArrayList<>();
        if (defenses.ac != null) {
            first.add(defenses.ac.toString());
        }
        if (defenses.savingThrows != null) {
            first.add(defenses.savingThrows.toString());
        }
        if (!first.isEmpty()) {
            lines.add(String.join("; ", first));
        }
        if (defenses.hpHardness != null) {
            second.add( "> "+ defenses.hpHardness.stream()
                .map(QuteDataHpHardness::toString)
                .collect(Collectors.joining("; ")));
        }
        if (isPresent(defenses.immunities)) {
            second.add("**Immunities** " + String.join(", ", defenses.immunities));
        }
        if(!second.isEmpty()){
            lines.add(String.join("; ",second));
        }
        if (isPresent(defenses.resistances)) {
            lines.add("> **Resistances** " + String.join(", ", defenses.resistances));
        }
        if (isPresent(defenses.weaknesses)) {
            lines.add("> **Weaknesses** " + String.join(", ", defenses.weaknesses));
        }
        return String.join("\n", lines);
    }

    private List<String> getAbilities(Collection<QuteAbility> abilities) {
        List<String> abilityString = new ArrayList<>();
        for(QuteAbility ability : abilities){
            String ab = " *** " +
                ability.getName() +
                " ***" +
                (ability.activity !=null ? ability.activity.toString() :"")+
                printIfNotNull(ability.getHasDetails(),"( "+ability.getBareTraitList())+") "+
                printIfNotNull(ability.cost)+
                printIfNotNull(ability.frequency)+
                printIfNotNull(ability.trigger)+
                printIfNotNull(ability.requirements)+
                printIfNotNull(ability.text).replaceAll("\n+","\n>")+
                printIfNotNull(ability.note);
            abilityString.add(ab);
        }
        return abilityString;
    }

    private String printIfNotNull(Boolean toPrint,String s ){
        return toPrint ? s : "";
    }
    private String printIfNotNull(String s){
        return printIfNotNull(s!=null,s);
    }
}
