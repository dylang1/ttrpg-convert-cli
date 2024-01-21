package dev.ebullient.convert;

import io.quarkus.test.junit.main.QuarkusMainIntegrationTest;
import org.junit.jupiter.api.BeforeAll;

@QuarkusMainIntegrationTest
public class Pf2VttDataConvertIT extends Pf2VttDataConvertTest {
    @BeforeAll
    public static void setupDir() {
        setupDir("RpgDataConvertIT");
    }
}
