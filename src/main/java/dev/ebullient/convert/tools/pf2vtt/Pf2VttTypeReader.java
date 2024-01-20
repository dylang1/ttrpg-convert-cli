package dev.ebullient.convert.tools.pf2vtt;

import com.fasterxml.jackson.databind.JsonNode;
import dev.ebullient.convert.tools.pf2vtt.JsonTextReplacement;

import java.util.Optional;

public interface Pf2VttTypeReader extends JsonSource{
    default String getOrdinalForm(String level) {
        return switch (level) {
            case "1" -> "1st";
            case "2" -> "2nd";
            case "3" -> "3rd";
            default -> level + "th";
        };
    }

    default String getFrequency(JsonNode node) {
        JsonNode frequency = JsonTextReplacement.Field.frequency.getFrom(node);
        if (frequency == null) {
            return null;
        }
        String special = JsonTextReplacement.Field.special.getTextOrNull(frequency);
        if (special != null) {
            return replaceText(special);
        }

        String number = numberToText(frequency, JsonTextReplacement.Field.number, true);
        String unit = JsonTextReplacement.Field.unit.getTextOrEmpty(frequency);
        String customUnit = JsonTextReplacement.Field.customUnit.getTextOrDefault(frequency, unit);
        Optional<Integer> interval = JsonTextReplacement.Field.interval.getIntFrom(frequency);
        boolean overcharge = JsonTextReplacement.Field.overcharge.booleanOrDefault(frequency, false);
        boolean recurs = JsonTextReplacement.Field.recurs.booleanOrDefault(frequency, false);

        return String.format("%s %s %s%s%s",
            number,
            recurs ? "every" : "per",
            interval.map(integer -> integer + " ").orElse(""),
            interval.isPresent() && interval.get() > 2 ? unit + "s" : customUnit,
            overcharge ? ", plus overcharge" : "");
    }

    default String numberToText(JsonNode baseNode, JsonTextReplacement.Field field, boolean freq) {
        JsonNode node = field.getFrom(baseNode);
        if (node == null) {
            throw new IllegalArgumentException("undefined or null object");
        } else if (node.isTextual()) {
            return node.asText();
        }
        int number = node.asInt();
        String numString = intToString(number, freq);

        return (number < 0 ? "negative " : "") + numString;
    }

    default String intToString(int number, boolean freq) {
        int abs = Math.abs(number);
        if (abs >= 100) {
            return abs + "";
        }
        switch (abs) {
            case 0 -> {
                return "zero";
            }
            case 1 -> {
                return freq ? "once" : "one";
            }
            case 2 -> {
                return freq ? "twice" : "two";
            }
            case 3 -> {
                return "three";
            }
            case 4 -> {
                return "four";
            }
            case 5 -> {
                return "five";
            }
            case 6 -> {
                return "six";
            }
            case 7 -> {
                return "seven";
            }
            case 8 -> {
                return "eight";
            }
            case 9 -> {
                return "nine";
            }
            case 10 -> {
                return "ten";
            }
            case 11 -> {
                return "eleven";
            }
            case 12 -> {
                return "twelve";
            }
            case 13 -> {
                return "thirteen";
            }
            case 14 -> {
                return "fourteen";
            }
            case 15 -> {
                return "fifteen";
            }
            case 16 -> {
                return "sixteen";
            }
            case 17 -> {
                return "seventeen";
            }
            case 18 -> {
                return "eighteen";
            }
            case 19 -> {
                return "nineteen";
            }
            case 20 -> {
                return "twenty";
            }
            case 30 -> {
                return "thirty";
            }
            case 40 -> {
                return "forty";
            }
            case 50 -> {
                return "fifty";
            }
            case 60 -> {
                return "sixty";
            }
            case 70 -> {
                return "seventy";
            }
            case 80 -> {
                return "eighty";
            }
            case 90 -> {
                return "ninety";
            }
            default -> {
                int r = abs % 10;
                return intToString(abs - r, freq) + "-" + intToString(r, freq);
            }
        }
    }
}
