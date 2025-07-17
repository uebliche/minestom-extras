package net.uebliche.minestom.extras.appleskin;

import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import net.uebliche.minestom.extras.appleskin.network.clientbound.ExhaustionPacket;
import net.uebliche.minestom.extras.appleskin.network.clientbound.NaturalRegenerationPacket;
import net.uebliche.minestom.extras.appleskin.network.clientbound.SaturationPacket;
import net.uebliche.minstom.extras.common.Extra;
import net.uebliche.minstom.extras.common.ExtraRegistry;
import net.uebliche.minstom.extras.common.Packet;

import java.util.function.Consumer;

/**
 * Extra class for AppleSkin integration.
 */
public class AppleSkinExtra extends Extra {

    /**
     * Constructor
     */
    public AppleSkinExtra() {
        super();
    }

    @Override
    protected void onRegister(ExtraRegistry registry) {
    }

    @Override
    protected void registerPluginMessagesTypes(Consumer<Class<? extends Packet>> packet) {
        packet.accept(ExhaustionPacket.class);
        packet.accept(NaturalRegenerationPacket.class);
        packet.accept(SaturationPacket.class);
    }

    @Override
    protected void registerDefaultPacketListerners(ExtraRegistry registry) {

    }

    @Override
    public EventNode<PlayerEvent> eventNode() {
        return EventNode.type("minestom-extras:appleskin", EventFilter.PLAYER)
                .addListener(PlayerPacketOutEvent.class, event -> {
                    if (event.getPacket() instanceof UpdateHealthPacket healthPacket) {
                        new SaturationPacket( healthPacket.foodSaturation()).send(event.getPlayer());
                    }
                });
    }

    /**
     * Send the packet to the player
     *
     * @param player     the player to send the packet to
     * @param exhaustion the exhaustion value
     */
    public void sendExhaustion(Player player, float exhaustion) {
        new ExhaustionPacket(exhaustion).send(player);
    }

    /**
     * Send the packet to the player
     *
     * @param player              the player to send the packet to
     * @param naturalRegeneration the natural regeneration value
     */
    public void sendNaturalRegeneration(Player player, boolean naturalRegeneration) {
        new NaturalRegenerationPacket(naturalRegeneration).send(player);
    }

    /***
     * Send the packet to the player
     * @param player the player to send the packet to
     * @param saturation the saturation value
     */
    public void sendSaturation(Player player, float saturation) {
        new SaturationPacket(saturation).send(player);
    }


}
