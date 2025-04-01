package net.uebliche.minestom.extras.appleskin;

import net.uebliche.minstom.extras.common.Packet;
import net.uebliche.minstom.extras.common.PacketInfo;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import org.jetbrains.annotations.NotNull;

/**
 * Packet to send the current exhaustion level to the client.
 * @param exhaustion the current exhaustion level
 */
@PacketInfo(channel = "appleskin:exhaustion")
public record ExhaustionPacket(
        float exhaustion
) implements Packet {

    public static final @NotNull NetworkBuffer.Type<ExhaustionPacket> SERIALIZER = NetworkBufferTemplate.template(
            NetworkBuffer.FLOAT, ExhaustionPacket::exhaustion,
            ExhaustionPacket::new
    );

}
