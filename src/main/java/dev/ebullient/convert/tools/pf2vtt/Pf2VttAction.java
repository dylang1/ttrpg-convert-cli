package dev.ebullient.convert.tools.pf2vtt;

import dev.ebullient.convert.tools.JsonNodeReader;

public enum Pf2VttAction implements JsonNodeReader {
    activity,
    actionType,
    cost,
    info,
    prerequisites,
    trigger
}

