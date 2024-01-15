package dev.ebullient.convert.tools.pf2e.qute;

import java.util.List;

import dev.ebullient.convert.tools.Tags;
import dev.ebullient.convert.tools.pf2e.Pf2VttSources;
import io.quarkus.qute.TemplateData;

/**
 * Pf2eTools Background attributes ({@code background2md.txt})
 * <p>
 * Extension of {@link dev.ebullient.convert.tools.pf2e.qute.Pf2eQuteBase Pf2eQuteBase}
 * </p>
 */
@TemplateData
public class QuteBackground extends Pf2eQuteBase {

    public QuteBackground(Pf2VttSources sources, List<String> text, Tags tags) {
        super(sources, text, tags);
    }
}
