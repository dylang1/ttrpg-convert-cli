package dev.ebullient.convert.tools.pf2e.qute;

import dev.ebullient.convert.qute.QuteUtil;
import dev.ebullient.convert.tools.Tags;
import dev.ebullient.convert.tools.pf2e.Pf2eSources;
import io.quarkus.qute.TemplateData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
    public final Collection<String> senses;
    public final Collection<String> languages;
    public final QuteAbilityScores abilityScores;
    public final QuteSkills skill;
    public final Collection<String> items;
    public final Collection<String> speed;
    public final Collection<QuteInlineAttack> attacks;
    public final Collection<QuteSpellcasting> spellcastings;
    public final Collection<QuteAbility> abilities;
    public final QuteDataDefenses defenses;


    public QuteCreature(Pf2eSources sources, List<String> text, Tags tags, Collection<String> traits, List<String> aliases, String description, Integer level, Integer perception, Collection<String> senses, Collection<String> languages, QuteAbilityScores abilityScores, QuteSkills skill, Collection<String> items, Collection<String> speed, Collection<QuteInlineAttack> attacks, Collection<QuteSpellcasting> spellcastings, Collection<QuteAbility> abilities, QuteDataDefenses defenses) {
        super(sources, text, tags);
        this.traits = traits;
        this.aliases = aliases;
        this.description = description;
        this.level = level;
        this.perception = perception;
        this.senses = senses;
        this.languages = languages;
        this.abilityScores = abilityScores;
        this.skill = skill;
        this.items = items;
        this.speed = speed;
        this.attacks = attacks;
        this.spellcastings = spellcastings;
        this.abilities = abilities;
        this.defenses = defenses;
    }

    @TemplateData
    @Builder
    @Data
    @AllArgsConstructor
    public class QuteAbilityScores implements QuteUtil {
        public int strength;
        public int constitution;
        public int dexterity;
        public int intelligence;
        public int wisdom;
        public int charisma;
    }

    @TemplateData
    @Builder
    @Data
    @AllArgsConstructor
    public class QuteSkills implements QuteUtil {
        public int athletics;
        public int diplomancy;
        public int medicine;
        public int religion;
        public int society;
    }

    @TemplateData
    @Data
    @AllArgsConstructor
    public class QuteSpellcasting implements QuteUtil {
        public String name;
        public String type;
        public String tradition;
        public int DC;
        public int FP;
        public Collection<String> spells;
    }
}
