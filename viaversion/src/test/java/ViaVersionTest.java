import net.uebliche.minestom.extras.viaversion.PlayerDetailsPacket;
import net.uebliche.minstom.extras.common.PacketInfo;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPluginMessageEvent;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.player.GameProfile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ExtraRegistryTest.class)
public class ViaVersionTest extends ExtraRegistryTest {

    @Test
    void playerDetailsPacketSerializer() {
        var details = new PlayerDetailsPacket("ViaVersion", "3.1.3", 707, "1.20.2 - 1.21.5");
        var serializer = PlayerDetailsPacket.SERIALIZER;
        var buffer = NetworkBuffer.resizableBuffer();
        serializer.write(buffer, details);

        var details2 = serializer.read(buffer);
        assertEquals(details2, details);
    }

    @Test
    void playerDetailsPacketHandler() {
        registry.setListener(PlayerDetailsPacket.class, (packet, player) -> {
            System.out.println("PlayerDetailsPacket received: " + packet);
        });

        var packet = new PlayerDetailsPacket("ViaVersion", "3.1.3", 707, "1.20.2 - 1.21.5");
        NetworkBuffer.Type<PlayerDetailsPacket> serializer = PlayerDetailsPacket.SERIALIZER;
        NetworkBuffer buffer = NetworkBuffer.resizableBuffer();
        buffer.write(serializer, packet);

        String channel = packet.getClass().getAnnotation(PacketInfo.class).channel();

        byte[] data = new byte[(int) buffer.writeIndex()];
        buffer.copyTo(0, data, 0, data.length);
        var event = new PlayerPluginMessageEvent(new Player(null, new GameProfile(UUID.randomUUID(), "Player")), channel, data);
        registry.eventNode().call(event);
    }

    @AfterAll
    static void teardown() {
        MinecraftServer.stopCleanly();

    }


}
