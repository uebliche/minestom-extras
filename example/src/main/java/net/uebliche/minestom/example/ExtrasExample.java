package net.uebliche.minestom.example;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.block.Block;
import net.uebliche.minestom.extras.simplevoicechat.SimpleVoiceChatExtra;
import net.uebliche.minestom.extras.simplevoicechat.network.clientbound.GroupCreatedPacket;
import net.uebliche.minstom.extras.common.ExtraRegistry;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Example for the Extras API
 */
public class ExtrasExample {
    /**
     * Main method
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        new ExtrasExample();
    }

    /**
     * Constructor
     */
    public ExtrasExample() {
        var server = MinecraftServer.init();
        var registry = new ExtraRegistry();
        MinecraftServer.getGlobalEventHandler().addChild(registry.eventNode());
        registry.setListener(GroupCreatedPacket.class, (packet, player) -> {
            System.out.println("Group created: " + packet.clientGroup().id());
        });
        registry.registerHelper(new SimpleVoiceChatExtra());
        var voiceChat = registry.getHelper(SimpleVoiceChatExtra.class);
        MinecraftServer.getGlobalEventHandler().addChild(voiceChat.server().eventNode());

        var instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setGenerator(unit -> {
            unit.modifier().fillHeight(-64, -63, Block.BEDROCK);
            unit.modifier().fillHeight(-63, -0, Block.STONE);
            unit.modifier().fillHeight(0, 3, Block.DIRT);
            unit.modifier().fillHeight(3, 4, Block.GRASS_BLOCK);
        });
        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent.class, event -> {
            event.setSpawningInstance(instance);
            event.getPlayer().setRespawnPoint(new Pos(ThreadLocalRandom.current().nextDouble(-10, 10), 5, ThreadLocalRandom.current().nextDouble(-10, 10)));
        });

        server.start("0.0.0.0", 25565);

    }
}
