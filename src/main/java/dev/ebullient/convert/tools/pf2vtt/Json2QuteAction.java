package dev.ebullient.convert.tools.pf2vtt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.ebullient.convert.tools.JsonTextConverter;
import dev.ebullient.convert.tools.Tags;
import dev.ebullient.convert.tools.pf2vtt.qute.QuteAction;
import dev.ebullient.convert.tools.pf2vtt.qute.QuteDataActivity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Json2QuteAction extends Json2QuteBase {

    public Json2QuteAction(Pf2VttIndex index, JsonNode node) {
        super(index, Pf2VttIndexType.action, node);
    }

    @Override
    protected QuteAction buildQuteResource() {
        Tags tags = new Tags(sources);
        List<String> text = new ArrayList<>();
        JsonNode systemNode = Field.system.getFrom(rootNode);
        appendToText(text, SourceField.entries.getFrom(rootNode), "##");
        text.add(Pf2VttAction.description.replaceTextFromField(systemNode,Field.value,this));
        return new QuteAction(
            getSources(), text, tags,
            Pf2VttAction.description.parseCost(systemNode),
            Pf2VttAction.description.parseTrigger(systemNode),
            collectTraitsFrom(systemNode, tags),
            Pf2VttAction.description.parsePrerequisite(systemNode),
            Pf2VttAction.description.parseRequirements(systemNode),
            getFrequency(rootNode),
            Pf2VttTypeReader.getQuteActivity(systemNode, this),
            Pf2VttTypeReader.getPublication(systemNode));
    }
}
