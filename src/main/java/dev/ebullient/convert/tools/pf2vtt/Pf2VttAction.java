package dev.ebullient.convert.tools.pf2vtt;

import com.fasterxml.jackson.databind.JsonNode;
import dev.ebullient.convert.tools.JsonNodeReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Pf2VttAction implements JsonNodeReader {
    actions,
    actionType,
    description;

    public String parseTrigger(JsonNode source){
        String preParsedTrigger = getFieldFrom(source, JsonTextReplacement.Field.value).asText("");
        String pattern = "<strong>Trigger</strong>(.*?)</p>";
        return parseRegex(preParsedTrigger,pattern);
    }
    public String parseRequirements(JsonNode source){
        String preParsedTrigger = getFieldFrom(source, JsonTextReplacement.Field.value).asText("");
        String pattern = "<strong>Requirements</strong>(.*?)</p>";
        return parseRegex(preParsedTrigger,pattern);
    }
    public String parseCost(JsonNode source){
        String preParsedTrigger = getFieldFrom(source, JsonTextReplacement.Field.value).asText("");
        String pattern = "<strong>Cost</strong>(.*?)</p>";
        return parseRegex(preParsedTrigger,pattern);
    }
    public String parsePrerequisite(JsonNode source){
        String preParsedTrigger = getFieldFrom(source, JsonTextReplacement.Field.value).asText("");
        String pattern = "<strong>Prerequisite</strong>(.*?)</p>";
        return parseRegex(preParsedTrigger,pattern);
    }
    public String parseDescription(JsonNode source){
        String preParsedText = getFieldFrom(source, JsonTextReplacement.Field.value).asText("");
    StringBuilder description =new StringBuilder();
    String[] lines = preParsedText.split("</p>");
    for(String line : lines){
        if(line.contains("<strong>Trigger</strong>")){
            continue;
        }
        if(line.contains("<strong>Frequency</strong>")){
            continue;
        }
        if(line.contains("<strong>Prerequisite</strong>")){
            continue;
        }
        if(line.contains("<strong>Cost</strong>")){
            continue;
        }
        if(line.contains("<strong>Requirements</strong>")){
            continue;
        }
        description.append(removeHtml(line.replaceAll("<strong>Effect</strong>","")));

    }
        //turn text into lines of text with <p> </p> surrounding each line
        return description.toString();

        //RETHINK THIS WHOLE THING probs need to treat <P> withouth <strong> as new lines essentially, should do that first
        // parse each <P> as a line then can decide if its Prereq or effect etc etc
    }
    private String removeHtml(String text){
       return text.replaceAll("<.*?>", "");
    }
    private String parseRegex(String text, String regex){
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        String cleanText = "";
        if (matcher.find()) {
            String group = matcher.group(1);
            cleanText = group.replaceAll("<.*?>", "").trim();
        }
        return cleanText;

    }
}

