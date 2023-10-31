package dev.ebullient.convert.tools.pf2e.qute;

import dev.ebullient.convert.qute.QuteUtil;
import io.quarkus.qute.TemplateData;

import java.util.Arrays;

@TemplateData
public class QuteDataAbilityMods implements QuteUtil {
    public int strength;
    public int constitution;
    public int dexterity;
    public int intelligence;
    public int wisdom;
    public int charisma;

    public QuteDataAbilityMods(int strength, int constitution, int dexterity, int intelligence, int wisdom, int charisma) {
        this.strength = strength;
        this.constitution = constitution;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
    }
    public QuteDataAbilityMods() {
    }
    private String modToString(int value){
        return String.format("%s%s",
            value >= 0 ? "+" : "",
            value);
    }


    public String toStringAsTable() {
        return modToString(strength) + "|" + modToString(dexterity) + "|" + (constitution) +"|"+ (intelligence) + "|" + (wisdom) + "|" + (charisma);
    }
    @Override
    public String toString(){
        String sb = "**Str** " +
            modToString(strength) +
            " **Dex** " +
            modToString(dexterity) +
            " **Con** " +
            modToString(constitution) +
            " **Int** " +
            modToString(intelligence) +
            " **Wis** " +
            modToString(wisdom) +
            " **Cha** " +
            modToString(charisma);
        return sb;
    }
    public int[] asArray(){
        return new int[]{strength,dexterity,constitution,intelligence,wisdom,charisma};
    }
    public QuteDataAbilityMods setStrength(int strength) {
        this.strength = strength;
        return this;
    }

    public QuteDataAbilityMods setConstitution(int constitution) {
        this.constitution = constitution;
        return this;
    }

    public QuteDataAbilityMods setDexterity(int dexterity) {
        this.dexterity = dexterity;
        return this;
    }

    public QuteDataAbilityMods setIntelligence(int intelligence) {
        this.intelligence = intelligence;
        return this;
    }

    public QuteDataAbilityMods setWisdom(int wisdom) {
        this.wisdom = wisdom;
        return this;
    }

    public QuteDataAbilityMods setCharisma(int charisma) {
        this.charisma = charisma;
        return this;
    }
}
