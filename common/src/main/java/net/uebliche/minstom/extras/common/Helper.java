package net.uebliche.minstom.extras.common;

import net.minestom.server.entity.Player;
import net.minestom.server.event.EventNode;

/**
 * Helper class for Minestom Extras
 */
public abstract class Helper {
    /**
     * Constructor for the Helper class
     */
    public Helper() {

    }

    /**
     * The ExtraRegistry that is used to register the packets
     */
    protected ExtraRegistry registry;

    /**
     * The EventNode that is used to register the events
     *
     * @return The EventNode that is used to register the events
     */
    protected abstract EventNode eventNode();

    /**
     * Helper Method to send a packet to a player
     *
     * @param player the player to send the packet to
     * @param packet the packet to send
     */
    protected final void sendPacket(Player player, Packet packet) {
        player.sendPacket(registry.write(packet));
    }


}
