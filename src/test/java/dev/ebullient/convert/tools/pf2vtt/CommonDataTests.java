package dev.ebullient.convert.tools.pf2vtt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dev.ebullient.convert.TestUtils;
import dev.ebullient.convert.config.CompendiumConfig.Configurator;
import dev.ebullient.convert.config.Datasource;
import dev.ebullient.convert.config.TtrpgConfig;
import dev.ebullient.convert.io.MarkdownWriter;
import dev.ebullient.convert.io.Templates;
import dev.ebullient.convert.io.Tui;
import io.quarkus.arc.Arc;
import org.junit.jupiter.api.AfterEach;

import java.nio.file.Path;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonDataTests {
    protected final Tui tui;
    protected final Configurator configurator;
    protected final Templates templates;
    protected Pf2VttIndex index;

    protected final Path outputPath;
    protected final TestInput variant;

    enum TestInput {
        all,
        partial,
        none;
    }

    public CommonDataTests(TestInput variant) throws Exception {
        tui = Arc.container().instance(Tui.class).get();
        tui.init(null, true, false);

        templates = Arc.container().instance(Templates.class).get();
        tui.setTemplates(templates);

        this.variant = variant;
        this.outputPath = TestUtils.OUTPUT_ROOT_PF2VTT.resolve(variant.name());
        tui.setOutputPath(outputPath);
        outputPath.toFile().mkdirs();

        TtrpgConfig.init(tui, Datasource.vttPf2);
        configurator = new Configurator(tui);

        index = new Pf2VttIndex(TtrpgConfig.getConfig());
        templates.setCustomTemplates(TtrpgConfig.getConfig());

        if (TestUtils.TOOLS_PATH_PF2VTT.toFile().exists()) {
            switch (variant) {
                case all:
                    configurator.setSources(List.of("*"));
                    break;
                case partial:
                    configurator.readConfiguration(TestUtils.TEST_RESOURCES.resolve("pf2vtt.json"));
                    break;
                case none:
                    // should be default (CRD)
                    break;
            }

//            for (String x : List.of("books.json",
//                    "book/book-crb.json", "book/book-gmg.json")) {
//                tui.readFile(TestUtils.TOOLS_PATH_PF2VTT.resolve(x), TtrpgConfig.getFixes(x), index::importTree);
//            }
            tui.readToolsDir(TestUtils.TOOLS_PATH_PF2VTT, index::importTree);
            index.prepare();
        }
    }

    @AfterEach
    public void cleanup() {
        tui.close();
        tui.setOutputPath(outputPath);
        configurator.setAlwaysUseDiceRoller(false);
        templates.setCustomTemplates(TtrpgConfig.getConfig());
    }

    public void testDataIndex_pf2e() throws Exception {
        if (TestUtils.TOOLS_PATH_PF2VTT.toFile().exists()) {
            Path full = outputPath.resolve("allIndex.json");
            index.writeFullIndex(full);

            Path filtered = outputPath.resolve("allSourceIndex.json");
            index.writeFilteredIndex(filtered);

            assertThat(full).exists();
            JsonNode fullIndex = Tui.MAPPER.readTree(full.toFile());
            ArrayNode fullIndexKeys = fullIndex.withArray("keys");
            assertThat(fullIndexKeys).isNotNull();
            assertThat(fullIndexKeys).isNotEmpty();
            assertThat(fullIndex.has("ability|activate an item|crb"));

            assertThat(filtered).exists();
            JsonNode filteredIndex = Tui.MAPPER.readTree(filtered.toFile());
            ArrayNode filteredIndexKeys = filteredIndex.withArray("keys");
            assertThat(filteredIndexKeys).isNotNull();
            assertThat(filteredIndexKeys).isNotEmpty();

            if (variant == TestInput.all) {
                assertThat(fullIndexKeys).isEqualTo(filteredIndexKeys);
            } else {
                assertThat(fullIndexKeys.size()).isGreaterThan(filteredIndexKeys.size());
            }
        }
    }

    public void testNotes_p2fe() throws Exception {
        Path rulesDir = outputPath.resolve(index.rulesFilePath());
        Path compendiumDir = outputPath.resolve(index.compendiumFilePath());

        if (TestUtils.TOOLS_PATH_PF2VTT.toFile().exists()) {
            MarkdownWriter writer = new MarkdownWriter(outputPath, templates, tui);
            index.markdownConverter(writer, TtrpgConfig.imageFallbackPaths())
                    .writeNotesAndTables()
                    .writeImages();

            TestUtils.assertDirectoryContents(rulesDir, tui);
            assertThat(rulesDir.resolve("conditions.md")).exists();
            assertThat(compendiumDir.resolve("skills.md")).exists();
        }
    }

    Path generateNotesForType(Pf2VttIndexType type) {
        return generateNotesForType(List.of(type)).values().iterator().next();
    }

    Map<Pf2VttIndexType, Path> generateNotesForType(List<Pf2VttIndexType> types) {
        Map<Pf2VttIndexType, Path> map = new HashMap<>();
        Set<Path> paths = new HashSet<>();
        types.forEach(t -> {
            Path p = outputPath.resolve(t.getFilePath(index))
                    .resolve(t.relativePath());
            map.put(t, p);
            paths.add(p);
        });

        if (TestUtils.TOOLS_PATH_PF2VTT.toFile().exists()) {
            paths.forEach(p -> TestUtils.deleteDir(p));

            MarkdownWriter writer = new MarkdownWriter(outputPath, templates, tui);
            index.markdownConverter(writer, TtrpgConfig.imageFallbackPaths())
                    .writeFiles(types);

            paths.forEach(p -> TestUtils.assertDirectoryContents(p, tui));
        }
        return map;
    }

}
