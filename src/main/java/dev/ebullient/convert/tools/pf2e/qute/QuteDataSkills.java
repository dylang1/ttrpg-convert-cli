package dev.ebullient.convert.tools.pf2e.qute;

import dev.ebullient.convert.qute.QuteUtil;
import io.quarkus.qute.TemplateData;

import java.util.Map;

@TemplateData
public class QuteDataSkills implements QuteUtil {
    public Map<String,Integer> acrobatics;

    public Map<String,Integer> arcana;
    public Map<String,Integer> athletics;
    public Map<String,Integer> crafting;
    public Map<String,Integer> deception;
    public Map<String,Integer> diplomacy;
    public Map<String,Integer> intimidation;
    public Map<String,Integer> lore;
    public Map<String,Integer> medicine;
    public Map<String,Integer> nature;
    public Map<String,Integer> occultism;
    public Map<String,Integer> performance;
    public Map<String,Integer> religion;
    public Map<String,Integer> society;
    public Map<String,Integer> stealth;
    public Map<String,Integer> survival;
    public Map<String,Integer> thievery;
    public QuteDataSkills() {
    }

    public QuteDataSkills setAcrobatics(Map<String, Integer> acrobatics) {
        this.acrobatics = acrobatics;
        return this;
    }

    public QuteDataSkills setArcana(Map<String, Integer> arcana) {
        this.arcana = arcana;
        return this;
    }

    public QuteDataSkills setAthletics(Map<String, Integer> athletics) {
        this.athletics = athletics;
        return this;
    }

    public QuteDataSkills setCrafting(Map<String, Integer> crafting) {
        this.crafting = crafting;
        return this;
    }

    public QuteDataSkills setDeception(Map<String, Integer> deception) {
        this.deception = deception;
        return this;
    }

    public QuteDataSkills setDiplomacy(Map<String, Integer> diplomacy) {
        this.diplomacy = diplomacy;
        return this;
    }

    public QuteDataSkills setIntimidation(Map<String, Integer> intimidation) {
        this.intimidation = intimidation;
        return this;
    }

    public QuteDataSkills setLore(Map<String, Integer> lore) {
        this.lore = lore;
        return this;
    }

    public QuteDataSkills setMedicine(Map<String, Integer> medicine) {
        this.medicine = medicine;
        return this;
    }

    public QuteDataSkills setNature(Map<String, Integer> nature) {
        this.nature = nature;
        return this;
    }

    public QuteDataSkills setOccultism(Map<String, Integer> occultism) {
        this.occultism = occultism;
        return this;
    }

    public QuteDataSkills setPerformance(Map<String, Integer> performance) {
        this.performance = performance;
        return this;
    }

    public QuteDataSkills setReligion(Map<String, Integer> religion) {
        this.religion = religion;
        return this;
    }

    public QuteDataSkills setSociety(Map<String, Integer> society) {
        this.society = society;
        return this;
    }

    public QuteDataSkills setStealth(Map<String, Integer> stealth) {
        this.stealth = stealth;
        return this;
    }

    public QuteDataSkills setSurvival(Map<String, Integer> survival) {
        this.survival = survival;
        return this;
    }

    public QuteDataSkills setThievery(Map<String, Integer> thievery) {
        this.thievery = thievery;
        return this;
    }
    @Override
    public String toString(){
        String s = " ";
        if(acrobatics != null && !acrobatics.isEmpty()){
            s+= "Acrobatics " + acrobatics.get("std");
        }
        return s;
    }

}
