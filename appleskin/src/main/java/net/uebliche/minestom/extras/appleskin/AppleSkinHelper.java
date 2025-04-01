package net.uebliche.minestom.extras.appleskin;

import net.uebliche.minstom.extras.common.Helper;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;

/**
 * Helper class for AppleSkin integration.
 */
public class AppleSkinHelper extends Helper {

    /**
     * Constructor
     */
    public AppleSkinHelper() {
        super();
    }

    @Override
    public EventNode<PlayerEvent> eventNode() {
        return EventNode.type("minestom-extras:appleskin", EventFilter.PLAYER)
                .addListener(PlayerPacketOutEvent.class, event -> {
                    if (event.getPacket() instanceof UpdateHealthPacket healthPacket) {
                        float saturation = healthPacket.foodSaturation();
                    }
                });
    }

    /**
     * Send the packet to the player
     * @param player the player to send the packet to
     * @param exhaustion the exhaustion value
     */
    public void sendExhaustion(Player player, float exhaustion) {
        sendPacket(player, new ExhaustionPacket(exhaustion));
    }

    /**
     * Send the packet to the player
     * @param player the player to send the packet to
     * @param naturalRegeneration the natural regeneration value
     */
    public void sendNaturalRegeneration(Player player, boolean naturalRegeneration) {
        sendPacket(player, new NaturalRegenerationPacket(naturalRegeneration));
    }

    /***
     * Send the packet to the player
     * @param player the player to send the packet to
     * @param saturation the saturation value
     */
    public void sendSaturation(Player player, float saturation) {
        sendPacket(player, new SaturationPacket(saturation));
    }


}
