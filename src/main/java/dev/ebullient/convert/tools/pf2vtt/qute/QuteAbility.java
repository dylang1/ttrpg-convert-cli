package dev.ebullient.convert.tools.pf2vtt.qute;

import dev.ebullient.convert.tools.Tags;
import dev.ebullient.convert.tools.pf2e.qute.Pf2VttQuteBase;
import dev.ebullient.convert.tools.pf2vtt.Pf2VttSources;

import java.util.List;
//TODO CHANGE TO INHERIT Pf2VttQuteNote and make a PF2VTTQuteNote
public class QuteAbility extends Pf2VttQuteBase {
    public QuteAbility(Pf2VttSources sources, List<String> text, Tags tags) {
        super(sources, text, tags);
    }

    public QuteAbility(Pf2VttSources sources, String text, Tags tags) {
        super(sources, text, tags);
    }

    public QuteAbility(Pf2VttSources sources, String name, String source, String text, Tags tags) {
        super(sources, name, source, text, tags);
    }
}
