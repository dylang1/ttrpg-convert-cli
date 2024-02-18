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
        if (Field.traits.getFieldFrom(systemNode, Field.value).isArray()) {
            ArrayNode n = (ArrayNode) Field.traits.getFieldFrom(systemNode, Field.value);
            for (JsonNode traitNode : n) {
                String trait = traitNode.asText();
                tags.add(trait);
            }
        }
        if("Aid".equals(rootNode.get("name").asText())){
            int x = 1+1;
        }
        return new QuteAction(
            getSources(), text, tags,
            Pf2VttAction.description.parseCost(systemNode),
            Pf2VttAction.description.parseTrigger(systemNode),
            collectTraitsFrom(rootNode, tags),
            Pf2VttAction.description.parsePrerequisite(systemNode),
            Pf2VttAction.description.parseRequirements(systemNode),
            getFrequency(rootNode),
            Pf2VttTypeReader.getQuteActivity(systemNode, this),
            Pf2VttTypeReader.getPublication(systemNode));
    }

//    @RegisterForReflection
//    static class ActionType {
//        public Boolean basic;
//        public Boolean item;
//        public Json2QuteAction.Skill skill;
//        public List<String> ancestry;
//        public List<String> archetype;
//        public List<String> heritage;
//        public List<String> versatileHeritage;
//        @JsonProperty("class")
//        public List<String> classType;
//        public List<String> subclass;
//        public List<String> variantrule;
//
//        public void addTags(JsonSource convert, Tags tags) {
//            if (isBasic()) {
//                tags.add("action", "basic");
//            }
//            if (isItem()) {
//                tags.add("action", "item");
//            }
//            if (ancestry != null) {
//                ancestry.forEach(c -> tags.add("action", "ancestry", c));
//            }
//            if (archetype != null) {
//                archetype.forEach(c -> tags.add("action", "archetype", c));
//            }
//            if (classType != null) {
//                classType.forEach(c -> tags.add("action", "class", c));
//            }
//        }
//
//        public boolean isBasic() {
//            return basic != null && basic;
//        }
//
//        public boolean isItem() {
//            return item != null && item;
//        }
//
//        public QuteAction.ActionType build(JsonSource convert) {
//            return new QuteAction.ActionType(isBasic(), isItem(),
//                skill == null ? null
//                    : skill.buildString(convert),
//                classType == null ? null
//                    : classType.stream()
//                    .map(s -> convert.linkify(Pf2VttIndexType.classtype, s))
//                    .collect(Collectors.toList()),
//                subclass == null ? null
//                    : subclass.stream()
//                    .map(this::createSubclassLink)
//                    .map(s -> convert.linkify(Pf2VttIndexType.classtype, s))
//                    .collect(Collectors.toList()),
//                archetype == null ? null
//                    : archetype.stream()
//                    .map(s -> convert.linkify(Pf2VttIndexType.archetype, s))
//                    .collect(Collectors.toList()),
//                ancestry == null ? null
//                    : ancestry.stream()
//                    .map(this::createAncestryLink)
//                    .map(s -> convert.linkify(Pf2VttIndexType.ancestry, s))
//                    .collect(Collectors.toList()),
//                heritage == null ? null
//                    : heritage.stream()
//                    .map(this::createHeritageLink)
//                    .collect(Collectors.toList()),
//                versatileHeritage == null ? null
//                    : versatileHeritage.stream()
//                    .map(this::createVersatileHeritageLink)
//                    .collect(Collectors.toList()),
//                variantrule == null ? null
//                    : variantrule.stream()
//                    .map(s -> convert.linkify(Pf2VttIndexType.variantrule, s))
//                    .collect(Collectors.toList()));
//        }
//
//        private String createSubclassLink(String subclassName) {
//            String[] cSrc = this.classType.get(0).split("\\|");
//            String[] scSrc = subclassName.split("\\|");
//            return String.format("%s|%s|%s|%s|%s",
//                cSrc[0],
//                cSrc.length > 1 ? cSrc[1] : "",
//                scSrc[0],
//                scSrc[0],
//                scSrc.length > 1 ? scSrc[1] : "");
//        }
//
//        private String createAncestryLink(String ancestry) {
//            String[] aSrc = ancestry.split("\\|");
//            return String.format("%s|%s",
//                aSrc[0],
//                aSrc.length > 1 ? aSrc[1] : "");
//        }
//
//        private String createHeritageLink(String heritage) {
//            String[] aSrc = this.ancestry.get(0).split("\\|");
//            String[] hSrc = heritage.split("\\|");
//            return String.format("%s|%s|%s|%s|%s|",
//                aSrc[0],
//                aSrc.length > 1 ? aSrc[1] : "",
//                hSrc[0],
//                hSrc[0],
//                hSrc.length > 1 ? hSrc[1] : "");
//        }
//
//        private String createVersatileHeritageLink(String versatile) {
//            String[] aSrc = (this.ancestry == null ? "Human|CRB" : this.ancestry.get(0))
//                .split("\\|");
//            String[] vSrc = versatile.split("\\|");
//            return String.format("%s|%s|%s|%s|%s|",
//                aSrc[0],
//                aSrc.length > 1 ? aSrc[1] : "",
//                vSrc[0],
//                vSrc[0],
//                vSrc.length > 1 ? vSrc[1] : "");
//        }
//    }

    @RegisterForReflection
    static class Skill {
        public List<String> trained;
        public List<String> untrained;
        public List<String> expert;
        public List<String> legendary;

        public String buildString(JsonSource convert) {
            List<String> allSkills = new ArrayList<>();
            if (untrained != null) {
                List<String> inner = new ArrayList<>();
                untrained.forEach(s -> inner.add(convert.linkify(Pf2VttIndexType.skill, s)));
                allSkills.add(String.format("%s (untrained)", String.join(", ", inner)));
            }
            if (trained != null) {
                List<String> inner = new ArrayList<>();
                trained.forEach(s -> inner.add(convert.linkify(Pf2VttIndexType.skill, s)));
                allSkills.add(String.format("%s (trained)", String.join(", ", inner)));
            }
            if (expert != null) {
                List<String> inner = new ArrayList<>();
                expert.forEach(s -> inner.add(convert.linkify(Pf2VttIndexType.skill, s)));
                allSkills.add(String.format("%s (expert)", String.join(", ", inner)));
            }
            if (legendary != null) {
                List<String> inner = new ArrayList<>();
                legendary.forEach(s -> inner.add(convert.linkify(Pf2VttIndexType.skill, s)));
                allSkills.add(String.format("%s (legendary)", String.join(", ", inner)));
            }
            return String.join("; ", allSkills);
        }
    }
}
