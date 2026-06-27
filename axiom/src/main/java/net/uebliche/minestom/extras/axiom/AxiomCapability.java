package net.uebliche.minestom.extras.axiom;

/**
 * Server-side Axiom compatibility capabilities.
 *
 * @param liveWorldEditing whether the integration may route live edit requests
 *                         through a server-authoritative edit service
 * @param blockUpdateSafe whether normal Minestom block updates are considered
 *                        safe for Axiom clients
 * @param maxBlocksPerEditBatch conservative batch limit integrations should
 *                              enforce before applying edits
 */
public record AxiomCapability(
        boolean liveWorldEditing,
        boolean blockUpdateSafe,
        int maxBlocksPerEditBatch
) {
    public AxiomCapability {
        if (maxBlocksPerEditBatch < 1) {
            throw new IllegalArgumentException("maxBlocksPerEditBatch must be positive");
        }
    }
}
