package dev.ebullient.convert.tools.pf2e.qute;

import dev.ebullient.convert.qute.QuteUtil;
import io.quarkus.qute.TemplateData;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@TemplateData
public class QuteDataSpellcasting implements QuteUtil {
    public String name;
    public String type;
    public String tradition;
    public int DC;
    public int FP;
    /** Map of spell level and spells(links) with count of castings appended */
    public Map<String,List<String>> spells;

    public QuteDataSpellcasting(String name, String type, String tradition, int DC, int FP, Map<String,List<String>> spells) {
        this.name = name;
        this.type = type;
        this.tradition = tradition;
        this.DC = DC;
        this.FP = FP;
        this.spells = spells;
    }

    public QuteDataSpellcasting() {
    }

    @Override
    public String toString() {
        return "QuteSpellcasting{" + "name='" + name + '\'' + ", type='" + type + '\'' + ", tradition='" + tradition + '\'' + ", DC=" + DC + ", FP=" + FP + ", spells=" + spells + '}';
    }

    public QuteDataSpellcasting setName(String name) {
        this.name = name;
        return this;
    }

    public QuteDataSpellcasting setType(String type) {
        this.type = type;
        return this;
    }

    public QuteDataSpellcasting setTradition(String tradition) {
        this.tradition = tradition;
        return this;
    }

    public QuteDataSpellcasting setDC(int DC) {
        this.DC = DC;
        return this;
    }

    public QuteDataSpellcasting setFP(int FP) {
        this.FP = FP;
        return this;
    }

    public QuteDataSpellcasting setSpells(Map<String,List<String>> spells) {
        this.spells = spells;
        return this;
    }
}
