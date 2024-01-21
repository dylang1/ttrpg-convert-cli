package dev.ebullient.convert.tools.pf2vtt.qute;

import dev.ebullient.convert.tools.Tags;
import dev.ebullient.convert.tools.pf2vtt.Pf2VttIndexType;
import io.quarkus.qute.TemplateData;

import java.util.Collection;
import java.util.List;

/**
 * Pf2eTools Attack attributes (inline/embedded, {@code inline-attack2md.txt})
 * <p>
 * Extension of {@link Pf2eQuteNote Pf2eQuteNote}
 * </p>
 */
@TemplateData
public class QuteInlineAttack extends Pf2VttQuteNote {

    /** Collection of traits (decorated links) */
    public final Collection<String> traits;
    public final String meleeOrRanged;
    /** Activity/Activation cost (as {@link dev.ebullient.convert.tools.pf2vtt.qute.QuteDataActivity QuteDataActivity}) */
    public final QuteDataActivity activity;
    public final String attack;
    public final String damage;

    public QuteInlineAttack(String name, List<String> text, Tags tags, Collection<String> traits,
                            String meleeOrRanged, String attack, String damage, QuteDataActivity activity) {
        super(Pf2VttIndexType.syntheticGroup, name, null, text, tags);
        this.traits = traits;
        this.meleeOrRanged = meleeOrRanged;
        this.attack = attack;
        this.damage = damage;
        this.activity = activity;
    }

    @Override
    public String template() {
        return "inline-attack2md.txt";
    }
}
