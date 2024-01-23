package dev.ebullient.convert;

import dev.ebullient.convert.io.Tui;
import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainLauncher;
import io.quarkus.test.junit.main.QuarkusMainTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusMainTest
public class Pf2VttDataConvertTest {
    static Path outputPath_pf2;
    static Tui tui;

    @BeforeAll
    public static void setupDir() {
        setupDir("Pf2VttDataConvertTest");
    }

    public static void setupDir(String root) {
        tui = new Tui();
        tui.init(null, false, false);
        outputPath_pf2 = TestUtils.OUTPUT_ROOT_PF2VTT.resolve(root).resolve("test-cli");
        outputPath_pf2.toFile().mkdirs();
    }

    @Test
    void testLiveData_Pf2VttAllSources(QuarkusMainLauncher launcher) {
        if (TestUtils.TOOLS_PATH_PF2VTT.toFile().exists()) {
            // All, I mean it. Really for real.. ALL.
            final Path allIndex = outputPath_pf2.resolve("all-index");
            TestUtils.deleteDir(allIndex);

            List<String> args = new ArrayList<>(List.of("--index", "--debug",
                    "-s", "ALL",
                    "-o", allIndex.toString(),
                    "-g", "pf2vtt",
                   TestUtils.TOOLS_PATH_PF2VTT.toString()));
//            args.addAll(TestUtils.getFilesFrom(TestUtils.TOOLS_PATH_PF2E.resolve("adventure"))
//                    .stream()
//                    .filter(x -> !x.endsWith("-id.json"))
//                    .toList());
            args.addAll(TestUtils.getFilesFrom(TestUtils.TOOLS_PATH_PF2VTT.resolve("book")));
            //args.addAll(TestUtils.getFilesFrom(TestUtils.TOOLS_PATH_PF2VTT.resolve("bestiary")).stream().filter(x-> x.contains("creatures-b2.json")).toList());

            LaunchResult result = launcher.launch(args.toArray(new String[0]));
            assertThat(result.exitCode())
                    .withFailMessage("Command failed. Output:%n%s", TestUtils.dump(result))
                    .isEqualTo(0);

            Tui tui = new Tui();
            tui.init(null, false, false);

            TestUtils.assertDirectoryContents(allIndex, tui, (p, content) -> {
                List<String> errors = new ArrayList<>();
                content.forEach(l -> TestUtils.checkMarkdownLinks(allIndex.toString(), p, l, errors));
                return errors;
            });
        }
    }
}
