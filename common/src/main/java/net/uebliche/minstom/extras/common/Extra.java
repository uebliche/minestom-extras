package net.uebliche.minstom.extras.common;

import net.minestom.server.entity.Player;
import net.minestom.server.event.EventNode;

import java.util.function.Consumer;

/**
 * Extra class for Minestom Extras
 */
public abstract class Extra {
    /**
     * Constructor for the Extra class
     */
    public Extra() {

    }

    protected abstract void onRegister(ExtraRegistry registry);

    /**
     * The ExtraRegistry that is used to register the packets
     */
    protected ExtraRegistry registry;


    protected abstract void registerPluginMessagesTypes(Consumer<Class<? extends Packet>> packet);

    protected abstract void registerDefaultPacketListerners(ExtraRegistry registry);

    /**
     * The EventNode that is used to register the events
     *
     * @return The EventNode that is used to register the events
     */
    protected abstract EventNode eventNode();

}
