package dev.ebullient.convert.tools.pf2e.qute;

import dev.ebullient.convert.qute.QuteUtil;
import io.quarkus.qute.TemplateData;

import java.util.Map;

@TemplateData
public class QuteDataSkills implements QuteUtil {
    public Map<String, String> acrobatics;

    public Map<String, String> arcana;
    public Map<String, String> athletics;
    public Map<String, String> crafting;
    public Map<String, String> deception;
    public Map<String, String> diplomacy;
    public Map<String, String> intimidation;
    public Map<String, String> lore;
    public Map<String, String> medicine;
    public Map<String, String> nature;
    public Map<String, String> occultism;
    public Map<String, String> performance;
    public Map<String, String> religion;
    public Map<String, String> society;
    public Map<String, String> stealth;
    public Map<String, String> survival;
    public Map<String, String> thievery;

    public QuteDataSkills() {
    }

    public QuteDataSkills setAcrobatics(Map<String, String> acrobatics) {
        this.acrobatics = acrobatics;
        return this;
    }

    public QuteDataSkills setArcana(Map<String, String> arcana) {
        this.arcana = arcana;
        return this;
    }

    public QuteDataSkills setAthletics(Map<String, String> athletics) {
        this.athletics = athletics;
        return this;
    }

    public QuteDataSkills setCrafting(Map<String, String> crafting) {
        this.crafting = crafting;
        return this;
    }

    public QuteDataSkills setDeception(Map<String, String> deception) {
        this.deception = deception;
        return this;
    }

    public QuteDataSkills setDiplomacy(Map<String, String> diplomacy) {
        this.diplomacy = diplomacy;
        return this;
    }

    public QuteDataSkills setIntimidation(Map<String, String> intimidation) {
        this.intimidation = intimidation;
        return this;
    }

    public QuteDataSkills setLore(Map<String, String> lore) {
        this.lore = lore;
        return this;
    }

    public QuteDataSkills setMedicine(Map<String, String> medicine) {
        this.medicine = medicine;
        return this;
    }

    public QuteDataSkills setNature(Map<String, String> nature) {
        this.nature = nature;
        return this;
    }

    public QuteDataSkills setOccultism(Map<String, String> occultism) {
        this.occultism = occultism;
        return this;
    }

    public QuteDataSkills setPerformance(Map<String, String> performance) {
        this.performance = performance;
        return this;
    }

    public QuteDataSkills setReligion(Map<String, String> religion) {
        this.religion = religion;
        return this;
    }

    public QuteDataSkills setSociety(Map<String, String> society) {
        this.society = society;
        return this;
    }

    public QuteDataSkills setStealth(Map<String, String> stealth) {
        this.stealth = stealth;
        return this;
    }

    public QuteDataSkills setSurvival(Map<String, String> survival) {
        this.survival = survival;
        return this;
    }

    public QuteDataSkills setThievery(Map<String, String> thievery) {
        this.thievery = thievery;
        return this;
    }

    @Override
    public String toString() {
        String s = buildSkillString(arcana, "Arcana")
            + buildSkillString(acrobatics, "Acrobatics")
            + buildSkillString(athletics, "Athletics")
            + buildSkillString(crafting, "Crafting")
            + buildSkillString(deception, "Deception")
            + buildSkillString(diplomacy, "Diplomacy")
            + buildSkillString(intimidation, "Intimidation")
            + buildSkillString(lore, "Lore")
            + buildSkillString(medicine, "Medicine")
            + buildSkillString(nature, "Nature")
            + buildSkillString(occultism, "Occultism")
            + buildSkillString(performance, "Performance")
            + buildSkillString(religion, "Religion")
            + buildSkillString(society, "Society")
            + buildSkillString(stealth, "Stealth")
            + buildSkillString(survival, "Survival")
            + buildSkillString(thievery, "Thievery");
        return s;
    }
    private String buildSkillString(Map skill, String skillText) {
        if (skill == null || skill.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try {

            sb.append(skillText);
            skill.forEach((k, v) -> {
                if (!k.equals("std")) {
                    sb.append("( ").append(k).append(" )");
                } else if (k.equals("note")) {
                    sb.append("( ").append(v).append(" )");
                } else {
                    sb.append(" +").append(v);
                }
            });
            sb.append(", ");
        } catch (Exception e) {
            System.out.printf("E");
        }
        return sb.toString();
    }
}
