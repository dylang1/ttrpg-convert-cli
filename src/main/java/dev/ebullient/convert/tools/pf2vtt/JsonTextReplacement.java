package dev.ebullient.convert.tools.pf2vtt;

import dev.ebullient.convert.config.CompendiumConfig;
import dev.ebullient.convert.io.Tui;
import dev.ebullient.convert.tools.JsonNodeReader;
import dev.ebullient.convert.tools.JsonTextConverter;
import dev.ebullient.convert.tools.pf2vtt.Pf2VttSources;

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
        add_hash
    }

    Pattern asPattern = Pattern.compile("\\{@as ([^}]+)}");
    Pattern runeItemPattern = Pattern.compile("\\{@runeItem ([^}]+)}");
    Pattern dicePattern = Pattern.compile("\\{@(dice|damage) ([^}]+)}");
    Pattern chancePattern = Pattern.compile("\\{@chance ([^}]+)}");
    Pattern notePattern = Pattern.compile("\\{@note (\\*|Note:)?\\s?([^}]+)}");
    Pattern quickRefPattern = Pattern.compile("\\{@quickref ([^}]+)}");

    Pf2VttIndex index();

    Pf2VttSources getSources();

    default Tui tui() {
        return cfg().tui();
    }

    default CompendiumConfig cfg() {
        return index().cfg();
    }
}
