package dev.ebullient.convert.tools.pf2e.qute;

import java.util.List;

import dev.ebullient.convert.qute.QuteBase;
import dev.ebullient.convert.tools.Tags;
import dev.ebullient.convert.tools.pf2vtt.Pf2VttSources;
import dev.ebullient.convert.tools.pf2vtt.Pf2VttIndexType;
import io.quarkus.qute.TemplateData;

/**
 * Attributes for notes that are generated from the Pf2eTools data.
 * This is a trivial extension of {@link dev.ebullient.convert.qute.QuteBase QuteBase}.
 * <p>
 * Notes created from {@code Pf2eQuteBase} will use a specific template
 * for the type. For example, {@code QuteBackground} will use {@code background2md.txt}.
 * </p>
 */
@TemplateData
public class Pf2VttQuteBase extends QuteBase {

    protected final Pf2VttIndexType type;

    public Pf2VttQuteBase(Pf2VttSources sources, List<String> text, Tags tags) {
        this(sources, sources.getName(), sources.getSourceText(), String.join("\n", text), tags);
    }

    public Pf2VttQuteBase(Pf2VttSources sources, String text, Tags tags) {
        this(sources, sources.getName(), sources.getSourceText(), text, tags);
    }

    public Pf2VttQuteBase(Pf2VttSources sources, String name, String source, String text, Tags tags) {
        super(sources, name, source, text, tags);
        this.type = sources.getType();
    }

    public String title() {
        return getName();
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
