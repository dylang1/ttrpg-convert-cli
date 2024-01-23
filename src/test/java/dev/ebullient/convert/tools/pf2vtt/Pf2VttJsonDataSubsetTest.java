package dev.ebullient.convert.tools.pf2vtt;

import dev.ebullient.convert.tools.pf2vtt.CommonDataTests.TestInput;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class Pf2VttJsonDataSubsetTest {

    static CommonDataTests commonTests;

    @BeforeAll
    public static void setupDir() throws Exception {
        commonTests = new CommonDataTests(TestInput.partial);
    }

    @Test
    public void testIndex_p2fe() throws Exception {
        commonTests.testDataIndex_pf2e();
    }

    @Test
    public void testAbility_p2fe() throws Exception {
        commonTests.generateNotesForType(Pf2VttIndexType.ability);
    }

    @Test
    public void testAction_p2fe() throws Exception {
        commonTests.generateNotesForType(Pf2VttIndexType.action);
    }

    @Test
    public void testAffliction_p2fe() throws Exception {
        commonTests.generateNotesForType(List.of(Pf2VttIndexType.curse, Pf2VttIndexType.disease));
    }

    @Test
    public void testArchetype_p2fe() throws Exception {
        commonTests.generateNotesForType(Pf2VttIndexType.archetype);
    }

    @Test
    public void testBackground_p2fe() throws Exception {
        commonTests.generateNotesForType(Pf2VttIndexType.background);
    }
    @Test
    public void testCreature_p2fe() throws Exception {
        commonTests.generateNotesForType(Pf2VttIndexType.creature);
    }
    @Test
    public void testDeity_p2fe() throws Exception {
        commonTests.generateNotesForType(Pf2VttIndexType.deity);
    }

    @Test
    public void testDomain_p2fe() throws Exception {
        commonTests.generateNotesForType(Pf2VttIndexType.domain);
    }

    @Test
    public void testFeat_p2fe() throws Exception {
        commonTests.generateNotesForType(Pf2VttIndexType.feat);
    }

    @Test
    public void testHazard_p2fe() throws Exception {
        commonTests.generateNotesForType(Pf2VttIndexType.hazard);
    }

    @Test
    public void testItem_p2fe() throws Exception {
        commonTests.configurator.setAlwaysUseDiceRoller(true);
        commonTests.generateNotesForType(Pf2VttIndexType.item);
    }

    @Test
    public void testRitual_p2fe() throws Exception {
        commonTests.generateNotesForType(Pf2VttIndexType.ritual);
    }

    @Test
    public void testSpell_p2fe() throws Exception {
        commonTests.generateNotesForType(Pf2VttIndexType.spell);
    }

    @Test
    public void testTable_p2fe() throws Exception {
        commonTests.generateNotesForType(Pf2VttIndexType.table);
    }

    @Test
    public void testTrait_p2fe() throws Exception {
        commonTests.generateNotesForType(Pf2VttIndexType.trait);
    }

    @Test
    public void testNotes_p2fe() throws Exception {
        commonTests.testNotes_p2fe();
    }
}
