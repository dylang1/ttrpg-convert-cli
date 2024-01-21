package dev.ebullient.convert.tools.pf2vtt;

import com.fasterxml.jackson.databind.JsonNode;
import dev.ebullient.convert.io.MarkdownWriter;
import dev.ebullient.convert.qute.QuteNote;
import dev.ebullient.convert.tools.IndexType;
import dev.ebullient.convert.tools.MarkdownConverter;
import dev.ebullient.convert.tools.pf2vtt.qute.Pf2VttQuteBase;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Pf2VttMarkdown implements MarkdownConverter {
    final Pf2VttIndex index;
    final MarkdownWriter writer;
    final Map<String, String> fallbackPaths;

    public Pf2VttMarkdown(Pf2VttIndex index, MarkdownWriter writer, Map<String, String> imageFallbackPaths) {
        this.index = index;
        this.writer = writer;
        this.fallbackPaths = imageFallbackPaths;
    }

    @Override
    public Pf2VttMarkdown writeAll() {
        return writeFiles(Stream.of(Pf2VttIndexType.values())
            .collect(Collectors.toList()));
    }

    @Override
    public Pf2VttMarkdown writeImages() {
        index.tui().copyImages(Pf2VttSources.getImages(), fallbackPaths);
        return this;
    }

    @Override
    public Pf2VttMarkdown writeFiles(IndexType type) {
        return writeFiles(List.of(type));
    }

    @Override
    public Pf2VttMarkdown writeFiles(List<? extends IndexType> types) {
        if (types == null) {
        } else {
            writePf2VttQuteBase(types.stream()
                .filter(x -> !((Pf2VttIndexType) x).useQuteNote())
                .collect(Collectors.toList()));
            writeNotesAndTables(types.stream()
                .filter(x -> ((Pf2VttIndexType) x).useQuteNote())
                .collect(Collectors.toList()));
        }
        return this;
    }

    private void writePf2VttQuteBase(List<? extends IndexType> types) {
        if (types != null && types.isEmpty()) {
            return;
        }

        List<Pf2VttQuteBase> compendium = new ArrayList<>();
        List<Pf2VttQuteBase> rules = new ArrayList<>();

        for (Map.Entry<String, JsonNode> e : index.filteredEntries()) {
            final String key = e.getKey();
            final JsonNode node = e.getValue();

            final Pf2VttIndexType type = Pf2VttIndexType.getTypeFromKey(key);
            if (types != null && !types.contains(type)) {
                continue;
            }

            // Moved to index type -- also used by embedded rendering
            Pf2VttQuteBase converted = type.convertJson2QuteBase(index, node);
            if (converted != null) {
                append(type, converted, compendium, rules);
            }
        }

        writer.writeFiles(index.compendiumFilePath(), compendium);
        writer.writeFiles(index.rulesFilePath(), rules);
    }

    @Override
    public Pf2VttMarkdown writeNotesAndTables() {
        return writeNotesAndTables(null);
    }

    private Pf2VttMarkdown writeNotesAndTables(List<? extends IndexType> types) {
        if (types != null && types.isEmpty()) {
            return this;
        }

        List<QuteNote> compendium = new ArrayList<>();
        List<QuteNote> rules = new ArrayList<>();

        Map<Pf2VttIndexType, Json2QuteBase> combinedDocs = new HashMap<>();
        for (Map.Entry<String, JsonNode> e : index.filteredEntries()) {
            final String key = e.getKey();
            final JsonNode node = e.getValue();

            final Pf2VttIndexType type = Pf2VttIndexType.getTypeFromKey(key);
            if (types != null && !types.contains(type)) {
                continue;
            }

//            switch (type) {
//                case ability -> rules.add(new Json2QuteAbility(index, type, node).buildNote());
//                case book -> {
//                    index.tui().printlnf("📖 Looking at book: %s", e.getKey());
//                    JsonNode data = index.getIncludedNode(key.replace("book|", "data|"));
//                    if (data == null) {
//                        index.tui().errorf("No data for %s", key);
//                    } else {
//                        List<Pf2eQuteNote> pages = new Json2QuteBook(index, type, node, data).buildBook();
//                        rules.addAll(pages);
//                    }
//                }
//                case condition -> {
//                    Json2QuteCompose conditions = (Json2QuteCompose) combinedDocs.computeIfAbsent(type,
//                        t -> new Json2QuteCompose(type, index, "Conditions"));
//                    conditions.add(node);
//                }
//                case domain -> {
//                    Json2QuteCompose domains = (Json2QuteCompose) combinedDocs.computeIfAbsent(type,
//                        t -> new Json2QuteCompose(type, index, "Domains"));
//                    domains.add(node);
//                }
//                case skill -> {
//                    Json2QuteCompose skills = (Json2QuteCompose) combinedDocs.computeIfAbsent(type,
//                        t -> new Json2QuteCompose(type, index, "Skills"));
//                    skills.add(node);
//                }
//                case table -> {
//                    Pf2eQuteNote table = new Json2QuteTable(index, node).buildNote();
//                    rules.add(table);
//                }
//                default -> {
//                }
//            }
        }

        for (Json2QuteBase value : combinedDocs.values()) {
            append(value.type, value.buildNote(), compendium, rules);
        }

        // Custom indices
//        append(Pf2VttIndexType.trait, Json2QuteTrait.buildIndex(index), compendium, rules);

        writer.writeNotes(index.compendiumFilePath(), compendium, true);
        writer.writeNotes(index.rulesFilePath(), rules, false);

        // TODO: DOES THIS WORK RIGHT? shouldn't these be in the other image map?
        // List<ImageRef> images = rules.stream()
        //         .flatMap(s -> s.images().stream()).collect(Collectors.toList());
        // index.tui().copyImages(images, fallbackPaths);
        return this;
    }

    <T> void append(Pf2VttIndexType type, T note, List<T> compendium, List<T> rules) {
        if (note != null) {
            if (type.useCompendiumBase()) {
                compendium.add(note);
            } else {
                rules.add(note);
            }
        }
    }
}
