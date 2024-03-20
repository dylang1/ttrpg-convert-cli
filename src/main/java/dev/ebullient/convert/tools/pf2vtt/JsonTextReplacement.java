package dev.ebullient.convert.tools.pf2vtt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.ebullient.convert.config.CompendiumConfig;
import dev.ebullient.convert.io.Tui;
import dev.ebullient.convert.tools.JsonNodeReader;
import dev.ebullient.convert.tools.JsonTextConverter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public interface JsonTextReplacement extends JsonTextConverter<Pf2VttIndexType> {

    enum Field implements JsonNodeReader {
        alias,
        auto,
        by,
        categories, // trait categories for indexing
        customUnit,
        data, // embedded data
        footnotes,
        frequency,
        group,
        head,
        interval,
        number,
        overcharge,
        range, // level effect
        recurs,
        reference,
        requirements,
        signature,
        special,
        style,
        tag, // embedded data
        title,
        traits,
        unit,
        add_hash,

        system,
        description,
        value
    }

    Pattern asPattern = Pattern.compile("\\{@as ([^}]+)}");
    Pattern runeItemPattern = Pattern.compile("\\{@runeItem ([^}]+)}");
    Pattern dicePattern = Pattern.compile("\\[\\[/r.*?\\]\\{(.*?)\\}");
    Pattern chancePattern = Pattern.compile("\\{@chance ([^}]+)}");
    Pattern notePattern = Pattern.compile("\\{@note (\\*|Note:)?\\s?([^}]+)}");
    Pattern quickRefPattern = Pattern.compile("\\{@quickref ([^}]+)}");

    Pattern paragraphPattern = Pattern.compile("<p>(.*?)</p>",Pattern.DOTALL);
    Pattern successDegPattern = Pattern.compile("<p><strong>(Critical Success|Success|Failure|Critical Failure)</strong>(.*)</p>");
Pattern linkPattern = Pattern.compile("@UUID\\[(.+?)\\]");
Pattern spanPattern = Pattern.compile("<span data-pf2-action=\"(.*?)\".*>(.*)</span>");
Pattern templatePattern = Pattern.compile("@Template\\[type:(\\w+\\b)\\|distance:(\\d+\\b)\\]");
Pattern damagePattern= Pattern.compile("@Damage\\[(.*?)d(.*?)\\[(.*?)\\]\\]");
    Pf2VttIndex index();

    Pf2VttSources getSources();

    default Tui tui() {
        return cfg().tui();
    }

    default CompendiumConfig cfg() {
        return index().cfg();
    }

//    default String formatDice(String diceRoll) {
////        int pos = diceRoll.indexOf(";");
////        if (pos >= 0) {
////            diceRoll = diceRoll.substring(0, pos);
////        }
//        return cfg().alwaysUseDiceRoller() && diceRoll.matches(JsonTextConverter.DICE_FORMULA)
//            ? "`dice: " + diceRoll + "` (`" + diceRoll + "`)"
//            : '`' + diceRoll + '`';
//    }

    default String replaceText(JsonNode input) {
        if (input == null) {
            return null;
        }
        if (input.isObject() || input.isArray()) {
            throw new IllegalArgumentException("Can only replace text for textual nodes: " + input);
        }
        return replaceText(input.asText());
    }

    default String replaceText(String input) {
        return replaceTokens(input, (s, b) -> this._replaceTokenText(s, b));
    }

    default String replaceTokens(String input, BiFunction<String, Boolean, String> tokenResolver) {
        if (input == null || input.isBlank()) {
            return input;
        }
        boolean foundDice = false;
//TODO: REVISIT THIS IF WE HAVE NESTED LINKS ETC but atm most of the parsing logic can exit in the resolver function
       //TODO: This should be changed to handle the @Damage[1d6[fire]] type stuff
        //
//        for (int i = 0; i < input.length(); i++) {
//            char c = input.charAt(i);
//            //char c2 = i + 1 < input.length() ? input.charAt(i + 1) : NUL;
//
//            switch (c) {
//                case '<':
//                    stack.push(buffer);
//                    buffer = new StringBuilder();
//                    buffer.append(c);
//                    break;
//                case '>':
//                    buffer.append(c);
//
//                    if('/' != buffer.charAt(buffer.length() -2)){
//                        //Skipping here until we find /> for the end of a html tag
//                        break;
//                    }
//                    String replace = tokenResolver.apply(buffer.toString(), stack.size() > 1);
//                    foundDice |= replace.contains("`dice:");
//                    if (stack.isEmpty()) {
//                        tui().warnf("Mismatched braces? Found '>' with an empty stack. Input: %s", input);
//                    } else {
//                        buffer = stack.pop();
//                    }
//                    buffer.append(replace);
//                    break;
//                default:
//                    buffer.append(c);
//                    break;
//            }
//        }
//
//        if (buffer.length() > 0) {
//            out.append(buffer);
//        }
        return foundDice
            ? simplifyFormattedDiceText(input)
            : tokenResolver.apply(input,false);
    }

    default String _replaceTokenText(String input, boolean nested) {
        if (input == null || input.isEmpty()) {
            return input;
        }
          try {
              //Removing stuff here as we are going to parse them to seperate members on the objects. to allow users more control
            String result = input
                .replaceAll("<p><strong>Prerequisite</strong>(.*?)</p>\n","")
                .replaceAll("<p><strong>Trigger</strong>(.*?)</p>\n","")
                .replaceAll("<p><strong>Requirements</strong>(.*?)</p>\n","")
                .replaceAll("<hr\\s*?/>\n","");
                if(successDegPattern.matcher(result).groupCount() >1){
                   result = successDegPattern.matcher(result)
                       .replaceFirst((match) -> "> [!success-degree] \n"+match.group(0));
                   result = successDegPattern.matcher(result)
                       .replaceAll((match)-> "> - **"+match.group(1) + "** " + match.group(2));
                }

                result = templatePattern.matcher(result).replaceAll(this::replaceTemplate);
//            if (parseState().inList() || parseState().inTable()) {
//                result = result.replaceAll("\\{@sup ([^}]+)}", "[^$1]");
//            } else {
//                result = result.replaceAll("\\{@sup ([^}]+)}", "[$1]: ");
//            }



            // TODO: review against Pf2e formatting patterns
//            if (cfg().alwaysUseDiceRoller()) {
//                result = replaceWithDiceRoller(result);
//            }

              result = dicePattern.matcher(result)
                  .replaceAll((match) -> formatDice(match.group(1)));
              result = damagePattern.matcher(result).replaceAll(this::replaceDamage);

//
//            result = chancePattern.matcher(result)
//                .replaceAll((match) -> match.group(1) + "% chance");
//
//            result = asPattern.matcher(result)
//                .replaceAll(this::replaceActionAs);

//            result = quickRefPattern.matcher(result)
//                .replaceAll((match) -> {
//                    String[] parts = match.group(1).split("\\|");
//                    if (parts.length > 4) {
//                        return parts[4];
//                    }
//                    return parts[0];
//                });
//
//            result = runeItemPattern.matcher(result)
//                .replaceAll(this::linkifyRuneItem);
//
//            result = Pf2VttIndexType.matchPattern.matcher(result)
//                .replaceAll(this::linkify);

            result = Pf2VttIndexType.linkPattern.matcher(result)
                .replaceAll(this::linkify);

            // "Style tags; {@bold some text to be bolded} (alternative {@b shorthand}),
            // {@italic some text to be italicised} (alternative {@i shorthand}),
            // {@underline some text to be underlined} (alternative {@u shorthand}),
            // {@strike some text to strike-through}, (alternative {@s shorthand}),
            // {@color color|e40707} tags, {@handwriting handwritten text},
            // {@sup some superscript,} {@sub some subscript,}
            // {@center some centered text} {@c with alternative shorthand,}
            // {@nostyle to escape font formatting} {@n (see below).}}
            // {@indentFirst You can use @indentFirst to indent the first line of text}
            // {@indentSubsequent is the counterpart to @indentFirst. }",

            try {
                result = result
                    .replaceAll("<p>(.*?)</p>", "$1\n")
                    .replaceAll("<strong>(.*?)</strong>","**$1**")
                    .replaceAll("<li>(.*?)</li>","- $1")
                    .replaceAll("<ul>","")
                    .replaceAll("</ul>","")
                    .replaceAll("<h2.*?>(.*?)</h2>","## $1");
            } catch (Exception e) {
                tui().errorf(e, "Unable to parse string from %s: %s", getSources().getKey(), input);
            }
            result = spanPattern.matcher(result)
                .replaceAll(this::replaceSpan);
              result = templatePattern.matcher(result)
                  .replaceAll(this::replaceTemplate);
            // second pass (nested references)
            result = Pf2VttIndexType.matchPattern.matcher(result)
                .replaceAll(this::linkify);


            // note pattern often wraps others. Do this one last.
            result = notePattern.matcher(result).replaceAll((match) -> {
                if (nested) {
                    return "***Note:** " + match.group(2).trim() + "*";
                } else {
                    List<String> text = new ArrayList<>();
                    text.add("> [!pf2-note]");
                    for (String line : match.group(2).split("\n")) {
                        text.add("> " + line);
                    }
                    return String.join("\n", text);
                }
            });

              if(result.contains("/>") || result.contains("/ >")){
                  tui().warnf("Found html tag not converted to markdown: %s",result);
              }
              if(result.contains("@")){
                  tui().warnf("Found Foundry Tag: %s",result);
              }
            return result;
        } catch (IllegalArgumentException e) {
            tui().errorf(e, "Failure replacing text: %s", e.getMessage());
        }

          if(input.contains("/>") || input.contains("/ >") || input.contains("<")){
              tui().warnf("Found html tag not converted to markdown: %s",input);
          }
        return input;
    }

    default String replaceSpan(MatchResult match){
        return String.format("[%s](%s)",match.group(2),match.group(1)
            .replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase());
    }

    default String replaceTemplate(MatchResult match){
        return String.format("%s-foot %s",match.group(2),match.group(1));
    }
    default String replaceDamage(MatchResult match){
        // defaulting to 1 here for cases where theres non digits in the number of dice
        //ie ceil(@actor.level/2)d8
        String damage = match.group(1).matches("[0-9]*") ?  match.group(1) : "1";
        Pattern pattern = Pattern.compile("\\D.*");
        String diceNo = match.group(2).replaceAll(pattern.pattern(),"");
//        String diceNo = Pattern.compile("([0-9]*)").matcher(match.group(2))
//            .replaceAll("$1");
        return String.format("`%sd%s %s`",damage,diceNo,match.group(3));
    }
    default String replaceFootnoteReference(MatchResult match) {
        return String.format("[^%s]%s", match.group(),
            parseState().inFootnotes() ? ": " : "");
    }

    default String replaceActionAs(MatchResult match) {
        final Pf2VttActivity type;
        switch (match.group(1).toLowerCase()) {
            case "1":
            case "a":
                type = Pf2VttActivity.single;
                break;
            case "2":
            case "d":
                type = Pf2VttActivity.two;
                break;
            case "3":
            case "t":
                type = Pf2VttActivity.three;
                break;
            case "f":
                type = Pf2VttActivity.free;
                break;
            case "r":
                type = Pf2VttActivity.reaction;
                break;
            default:
                type = Pf2VttActivity.passive;
                break;
        }
        return type.linkify(index().rulesVaultRoot());
    }

    default String linkifyRuneItem(MatchResult match) {
        String[] parts = match.group(1).split("\\|");
        String linkText = parts[0];
        // TODO {@runeItem longsword||+1 weapon potency||flaming|},
        // {@runeItem buugeng|LOAG|+3 weapon potency||optional display text}.
        // In general, the syntax is this:
        // (open curly brace)@runeItem base item|base item source|rune 1|rune 1
        // source|rune 2|rune 2 source|...|rune n|rune n source|display text(close curly
        // brace).
        // For each source, we assume CRB by default.",

        tui().debugf("TODO RuneItem found: %s", match);
        return linkText;
    }

    default String linkify(MatchResult match) {
        Pf2VttIndexType targetType = Pf2VttIndexType.fromText(match.group(1).split("\\.")[2]);
        if (targetType == null) {
            throw new IllegalStateException("Unknown type to linkify (how?)" + match.group(1));
        }
        return linkify(targetType, match.group(1));
    }

    default String linkify(Pf2VttIndexType targetType, String match) {
        if (match == null || match.isEmpty()) {
            return match;
        }
        String initLinkTest = match.substring(match.lastIndexOf(".")+1);
        switch (targetType) {
            case skill:
                // "Skill tags; {@skill Athletics}, {@skill Lore}, {@skill Perception}",
                // {@skill Lore||Farming Lore}
                String[] parts = match.split("\\|");
                String linkText = parts.length > 2 ? parts[2] : parts[0];
                return linkifyRules(Pf2VttIndexType.skill, linkText, "skills", toTitleCase(parts[0]));
            case classtype:
                return linkifyClass(match);
            case classFeature:
                return linkifyClassFeature(match);
            case subclassFeature:
                return linkifySubClassFeature(match);
            case trait:
                return linkifyTrait(match);
            case action:

                return linkifyRules(Pf2VttIndexType.action,initLinkTest,"actions","");
            case ignore:
                //Ignoring these ones as they are foundry effects or macros, which dont make sense for us to link to
                return "";
            default:
                tui().debugf("TODO Unsupported Linking found: %s for data %s",targetType.toString(), match);
                break;
        }

        // {@action strike}
        // {@action act together|som} can have sources added with a pipe,
        // {@action devise a stratagem|apg|and optional link text added with another pipe}.",
        // {@condition stunned} assumes CRB by default,
        // {@condition stunned|crb} can have sources added with a pipe (not that it's ever useful),
        // {@condition stunned|crb|and optional link text added with another pipe}
        // {@table ability modifiers} assumes CRB by default,
        // {@table automatic bonus progression|gmg} can have sources added with a pipe,
        // {@table domains|logm|and optional link text added with another pipe}.",
        String[] parts = match.split("\\|");
        String linkText = parts.length > 2 ? parts[2] : parts[0];
        String source = targetType.defaultSourceString();

        if (linkText.matches("\\[.+]\\(.+\\)")) {
            // skip if already a link
            return linkText;
        }
        if (targetType == Pf2VttIndexType.domain) {
            parts[0] = parts[0].replaceAll("\\s+\\([Aa]pocryphal\\)", "");
            return linkifyRules(Pf2VttIndexType.domain, linkText, "domains", toTitleCase(parts[0]));
        } else if (targetType == Pf2VttIndexType.condition) {
            String lT = linkText.substring(linkText.lastIndexOf(".")+1);
            return linkifyRules(Pf2VttIndexType.condition, lT, "conditions", toTitleCase(lT));
        }

        if (parts.length > 1) {
            source = parts[1].isBlank() ? source : parts[1];
        }

        if (targetType == Pf2VttIndexType.spell) {
            parts[0] = parts[0].replaceAll("\\s+\\((.*)\\)$", "-$1");
        }

        // TODO: aliases?
        String key = targetType.createKey(parts[0], source);

        // TODO: nested file structure for some types
        String link = String.format("[%s](%s/%s%s.md)",
            linkText,
            targetType.relativeRepositoryRoot(index()),
            slugify(parts[0]),
            targetType.isDefaultSource(source) ? "" : "-" + slugify(source));

        // if (targetType != Pf2eIndexType.action
        // && targetType != Pf2eIndexType.spell
        // && targetType != Pf2eIndexType.feat
        // && targetType != Pf2eIndexType.trait) {
        // tui().debugf("LINK for %s (%s): %s", match, index().isIncluded(key), link);
        // }
        return index().isIncluded(key) ? link : linkText;
    }

    default String linkifyTrait(String match) {
        // {@trait fire} does not require sources for official sources,
        // {@trait brutal|b2} can have sources added with a pipe in case of homebrew or duplicate trait names,
        // {@trait agile||and optional link text added with another pipe}.",

        String[] parts = match.split("\\|");
        String linkText = parts.length > 2 ? parts[2] : parts[0];

        if (parts.length < 2 && linkText.contains("<")) {
            String[] pieces = parts[0].split(" ");
            parts[0] = pieces[0];
        } else if (parts[0].startsWith("[")) {
            // Do the same replacement we did when doing the initial import
            // [...] becomes "Any ..."
            parts[0] = parts[0].replaceAll("\\[(.*)]", "Any $1");
        } else if (parts[0].length() <= 2) {
//            Alignment should be gone, TODO CHECK
//            Pf2eTypeReader.Pf2eAlignmentValue alignment = Pf2VttTypeReader.Pf2eAlignmentValue.fromString(parts[0]);
//            parts[0] = alignment == null ? parts[0] : alignment.longName;
        }

        String source = parts.length > 1 ? parts[1] : index().traitToSource(parts[0]);
        String key = Pf2VttIndexType.trait.createKey(parts[0], source);
        JsonNode traitNode = index().getIncludedNode(key);
        return linkifyTrait(traitNode, linkText);
    }

    default String linkifyTrait(JsonNode traitNode, String linkText) {
        if (traitNode != null) {
            String source = SourceField.source.getTextOrEmpty(traitNode);
            List<String> categories = Field.categories.getListOfStrings(traitNode, tui())
                .stream()
                .filter(x -> !"_alignAbv".equals(x))
                .toList();

            String title;
            if (categories.contains("Alignment")) {
                title = "Alignment";
            } else if (categories.contains("Rarity")) {
                title = "Rarity";
            } else if (categories.contains("Size")) {
                title = "Size";
            } else {
                title = categories.stream().sorted().findFirst().orElse("");
            }
            title = (SourceField.name.getTextOrEmpty(traitNode) + " " + title + " Trait").trim();

            return String.format("[%s](%s/%s%s.md \"%s\")",
                linkText,
                Pf2VttIndexType.trait.relativeRepositoryRoot(index()),
                slugify(linkText),
                Pf2VttIndexType.trait.isDefaultSource(source) ? "" : "-" + slugify(source),
                title.trim());
        }
        return linkText;
    }

    default String linkifyRules(Pf2VttIndexType type, String text, String rules, String anchor) {
        if (text.matches("\\[.+]\\(.+\\)")) {
            // skip if already a link
            return text;
        }
        return String.format("[%s](%s/%s.md)",
            text,
            type.relativeRepositoryRoot(index()),
            Tui.slugify(text));
    }

    default String linkifyClass(String match) {
        // "{@b Classes:}
        // {@class alchemist} assumes CRB by default,
        // {@class investigator|apg} can have sources added with a pipe,
        // {@class summoner|som|optional link text added with another pipe},
        // {@class barbarian|crb|subclasses added|giant} with another pipe,
        // {@class barbarian|crb|and class feature added|giant|crb|2-2} with another
        // pipe
        // (first number is level index (0-19), second number is feature index (0-n)),
        // although this is prone to changes in the index, it's best to use the above
        // method instead.",
        String[] parts = match.split("\\|");
        String className = parts[0];
        String classSource = String.valueOf(Pf2VttIndexType.classtype.defaultSource());
        String linkText = className;
        String subclass = null;
        if (parts.length > 3) {
            subclass = parts[3];
        }
        if (parts.length > 2) {
            linkText = parts[2];
        }
        if (parts.length > 1) {
            classSource = parts[1];
        }

        tui().debugf("TODO CLASS found: %s", match);
        return linkText;
    }

    default String linkifyClassFeature(String match) {
        // "{@b Class Features:}
        // {@classFeature rage|barbarian||1},
        // {@classFeature precise strike|swashbuckler|apg|1},
        // {@classFeature arcane spellcasting|magus|som|1|som},
        // {@classFeature rage|barbarian||1||optional display text}.
        // Class source is assumed to be CRB. Class feature source is assumed to be the
        // same as class source.",
        tui().debugf("TODO CLASS FEATURE found: %s", match);
        return match;
    }

    default String linkifySubClassFeature(String match) {
        // "{@b Subclass Features:}
        // {@subclassFeature research field|alchemist||bomber||1},
        // {@subclassFeature methodology|investigator|apg|empiricism|apg|1},
        // {@subclassFeature methodology|investigator|apg|empiricism|apg|1||and optional
        // display text} Class and Class feature source is assumed to be CRB.",
        tui().debugf("TODO CLASS FEATURE found: %s", match);
        return match;
    }

    default String joinAndReplace(JsonNode jsonSource, String field) {
        JsonNode node = jsonSource.get(field);
        if (node == null || node.isNull()) {
            return "";
        } else if (node.isTextual()) {
            return node.asText();
        } else if (node.isObject()) {
            throw new IllegalArgumentException(
                String.format("Unexpected object node (expected array): %s (referenced from %s)", node,
                    getSources()));
        }
        return joinAndReplace((ArrayNode) node);
    }

    default String joinAndReplace(JsonNode node) {
        if(!node.isArray()){
            throw new IllegalArgumentException(
                String.format("Unexpected object node (expected array): %s (referenced from %s)", node,
                    getSources()));
        }
        List<String> list = new ArrayList<>();
        node.forEach(v -> list.add(replaceText(v.asText())));
        return String.join(", ", list);
    }

}
