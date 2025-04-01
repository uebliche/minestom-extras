package net.uebliche.minstom.extras.common;

import net.minestom.server.entity.Player;

/**
 * Handler for packets
 *
 * @param <T> - the packet type
 */
@FunctionalInterface
public interface Handler<T> {

    /**
     * Handle the packet
     *
     * @param packet - the packet
     * @param player - the player
     */
    void accept(T packet, Player player);

}
