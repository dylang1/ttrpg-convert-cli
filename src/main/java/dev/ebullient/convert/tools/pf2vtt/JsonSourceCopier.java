package dev.ebullient.convert.tools.pf2vtt;

import com.fasterxml.jackson.databind.JsonNode;
import dev.ebullient.convert.tools.pf2vtt.Pf2VttSources;

public class JsonSourceCopier implements JsonSource {
    final Pf2VttIndex index;

    JsonSourceCopier(Pf2VttIndex index) {
        this.index = index;
    }

    @Override
    public Pf2VttIndex index() {
        return index;
    }

    @Override
    public Pf2VttSources getSources() {
        throw new IllegalStateException("Should not call getSources while copying source");
    }

    JsonNode handleCopy(Pf2VttIndex type, JsonNode jsonSource) {
        return jsonSource;
    }
}
