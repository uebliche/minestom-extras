package net.uebliche.minestom.extras.axiom;

import net.minestom.server.event.EventNode;
import net.uebliche.minstom.extras.common.Extra;
import net.uebliche.minstom.extras.common.ExtraRegistry;
import net.uebliche.minstom.extras.common.Packet;

import java.util.function.Consumer;

/**
 * Compatibility marker and policy surface for Axiom world-edit workflows.
 *
 * <p>Axiom is primarily a creator-side world editing client. This extra does not
 * trust client edits directly and does not register custom channels by default;
 * it gives Minestom integrations a single place to advertise conservative
 * server-side capabilities and limits before routing edit requests through their
 * own permission-checked world manipulation service.</p>
 */
public class AxiomExtra extends Extra {

    public static final String EXTRA_ID = "minestom-extras:axiom";
    private final AxiomCapability capability;

    public AxiomExtra() {
        this(new AxiomCapability(true, true, 4096));
    }

    public AxiomExtra(AxiomCapability capability) {
        this.capability = capability;
    }

    public AxiomCapability capability() {
        return capability;
    }

    public boolean supportsLiveWorldEditing() {
        return capability.liveWorldEditing();
    }

    public int maxBlocksPerEditBatch() {
        return capability.maxBlocksPerEditBatch();
    }

    @Override
    protected void onRegister(ExtraRegistry registry) {
        // No eager state is needed. Server implementations should wire Axiom
        // actions into their own permission-checked edit service.
    }

    @Override
    protected void registerPluginMessagesTypes(Consumer<Class<? extends Packet>> packet) {
        // Axiom compatibility uses normal server-authoritative world updates.
    }

    @Override
    protected void registerDefaultPacketListerners(ExtraRegistry registry) {
        // No default client-originated mutations are accepted.
    }

    @Override
    protected EventNode eventNode() {
        return EventNode.all(EXTRA_ID);
    }
}
