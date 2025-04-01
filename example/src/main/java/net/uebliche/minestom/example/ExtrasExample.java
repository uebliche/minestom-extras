package net.uebliche.minestom.example;

import net.uebliche.minstom.extras.common.ExtraRegistry;
import net.minestom.server.MinecraftServer;

/**
 * Example for the Extras API
 */
public class ExtrasExample {
    /**
     * Main method
     * @param args the arguments
     */
    public static void main(String[] args) {
        new ExtrasExample();
    }

    /**
     * Constructor
     */
    public ExtrasExample() {
        MinecraftServer.init();
        var registry =new ExtraRegistry();
        MinecraftServer.getGlobalEventHandler().addChild(registry.eventNode());


    }
}
