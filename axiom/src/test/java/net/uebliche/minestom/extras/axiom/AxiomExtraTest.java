package net.uebliche.minestom.extras.axiom;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AxiomExtraTest {

    @Test
    void exposesConservativeDefaultCapabilities() {
        AxiomExtra extra = new AxiomExtra();

        assertTrue(extra.supportsLiveWorldEditing());
        assertTrue(extra.capability().blockUpdateSafe());
        assertEquals(4096, extra.maxBlocksPerEditBatch());
    }

    @Test
    void rejectsNonPositiveBatchLimits() {
        assertThrows(IllegalArgumentException.class, () -> new AxiomCapability(true, true, 0));
    }
}
