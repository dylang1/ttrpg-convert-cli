package dev.ebullient.convert.tools.pf2vtt.qute;

import dev.ebullient.convert.qute.QuteNote;
import dev.ebullient.convert.tools.Tags;
import dev.ebullient.convert.tools.pf2vtt.Pf2VttIndexType;
import dev.ebullient.convert.tools.pf2vtt.Pf2VttSources;
import io.quarkus.qute.TemplateData;

import java.util.List;

/**
 * Attributes for notes that are generated from the Pf2eTools data.
 * This is a trivial extension of {@link QuteNote QuteNote}.
 * <p>
 * Notes created from {@code Pf2eQuteNote} will use the {@code note2md.txt} template
 * unless otherwise noted. Folder index notes use {@code index2md.txt}.
 * </p>
 */
@TemplateData
public class Pf2VttQuteNote extends QuteNote {
    final Pf2VttIndexType type;

    public Pf2VttQuteNote(Pf2VttIndexType type, String name, String sourceText, List<String> text, Tags tags) {
        this(type, name, sourceText, String.join("\n", text), tags);
    }

    public Pf2VttQuteNote(Pf2VttIndexType type, Pf2VttSources sources, String name, List<String> text, Tags tags) {
        super(sources, name, sources.getSourceText(), String.join("\n", text), tags);
        this.type = type;
    }

    public Pf2VttQuteNote(Pf2VttIndexType type, String name, String sourceText, String text, Tags tags) {
        super(name, sourceText, text, tags);
        this.type = type;
    }

    public Pf2VttQuteNote(Pf2VttIndexType type, Pf2VttSources sources, String text, Tags tags) {
        super(sources, sources.getName(), sources.getSourceText(), text, tags);
        this.type = type;
    }

    public Pf2VttQuteNote(Pf2VttIndexType type, Pf2VttSources sources, String name) { // custom indexes
        super(sources, name, sources.getSourceText(), null, null);
        this.type = type;
    }

    public Pf2VttIndexType type() {
        return type;
    }

    @Override
    public String targetPath() {
        return type.relativePath();
    }

    @Override
    public String targetFile() {
        if (sources != null && !type.defaultSource().sameSource(sources.primarySource())) {
            return getName() + "-" + sources.primarySource();
        }
        return getName();
    }
}
