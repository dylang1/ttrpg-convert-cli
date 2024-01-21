package dev.ebullient.convert.tools.pf2vtt;

import com.fasterxml.jackson.databind.JsonNode;
import dev.ebullient.convert.tools.JsonNodeReader;
import dev.ebullient.convert.tools.pf2vtt.qute.Pf2VttQuteBase;
import dev.ebullient.convert.tools.pf2vtt.qute.Pf2VttQuteNote;

import java.util.List;
import java.util.stream.Collectors;

public class Json2QuteBase implements Pf2VttTypeReader {
    protected final Pf2VttIndex index;
    protected final Pf2VttIndexType type;
    protected final JsonNode rootNode;
    protected final Pf2VttSources sources;

    public Json2QuteBase(Pf2VttIndex index, Pf2VttIndexType type, JsonNode rootNode) {
        this(index, type, rootNode, Pf2VttSources.findOrTemporary(type, rootNode));
    }

    public Json2QuteBase(Pf2VttIndex index, Pf2VttIndexType type, JsonNode rootNode, Pf2VttSources sources) {
        this.index = index;
        this.type = type;
        this.rootNode = rootNode;
        this.sources = sources;
    }

    @Override
    public Pf2VttIndex index() {
        return index;
    }

    @Override
    public Pf2VttSources getSources() {
        return sources;
    }

    List<String> toAlignments(JsonNode alignNode, JsonNodeReader alignmentField) {
        return alignmentField.getListOfStrings(alignNode, tui()).stream()
            .map(a -> a.length() > 2 ? a : linkify(Pf2VttIndexType.trait, a.toUpperCase()))
            .collect(Collectors.toList());
    }

    public Pf2VttQuteBase build() {
        boolean pushed = parseState().push(getSources(), rootNode);
        try {
            return buildQuteResource();
        } finally {
            parseState().pop(pushed);
        }
    }

    public Pf2VttQuteNote buildNote() {
        boolean pushed = parseState().push(getSources(), rootNode);
        try {
            return buildQuteNote();
        } finally {
            parseState().pop(pushed);
        }
    }

    protected Pf2VttQuteBase buildQuteResource() {
        tui().warnf("The default buildQuteResource method was called for %s. Was this intended?", sources.toString());
        return null;
    }

    protected Pf2VttQuteNote buildQuteNote() {
        tui().warnf("The default buildQuteNote method was called for %s. Was this intended?", sources.toString());
        return null;
    }
}
