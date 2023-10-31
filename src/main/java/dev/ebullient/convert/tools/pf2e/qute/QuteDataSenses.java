package dev.ebullient.convert.tools.pf2e.qute;

import dev.ebullient.convert.qute.QuteUtil;
import io.quarkus.qute.TemplateData;

import java.util.StringJoiner;

@TemplateData
public class QuteDataSenses implements QuteUtil {
    String name;
    String type;
    String range;

    public QuteDataSenses(String name, String type, String range) {
        this.name = name;
        this.type = type;
        this.range = range;
    }

    public QuteDataSenses setName(String name) {
        this.name = name;
        return this;
    }

    public QuteDataSenses setType(String type) {
        this.type = type;
        return this;
    }

    public QuteDataSenses setRange(String range) {
        this.range = range;
        return this;
    }
    @Override
    public String toString(){
        String s = " "+ name;
        if(type != null){
            s += " (" +type+")";
        }
        if(range != null){
            s+=" " + range +" feet";
        }
        return s;
    }
}
