package dev.ebullient.convert.tools.pf2e.qute;

import dev.ebullient.convert.tools.Tags;
import dev.ebullient.convert.tools.pf2e.Pf2eSources;
import io.quarkus.qute.TemplateData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public QuteCreature(Pf2eSources sources, String name, String source, String text, Tags tags, List<String> aliases, Collection<String> traits, String description, Integer level, Integer perception, Collection<QuteDataSenses> senses, String languages, QuteDataAbilityMods abilityMods, QuteDataSkills skills, Collection<String> items, String speed, Collection<QuteInlineAttack> attacks, Collection<QuteDataSpellcasting> spellcastings, Collection<QuteAbility> topAbilities, Collection<QuteAbility> midAbilities, Collection<QuteAbility> botAbilities, QuteDataDefenses defenses) {
        super(sources, name, source, text, tags);
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

    public Collection<String> attacks(){
        List<String> attackString = new ArrayList<>();
        for (QuteInlineAttack attack: attacks ) {
            StringBuilder sb = new StringBuilder();
            //sb.append()

        }
        return attackString;
    }
}
