package net.uebliche.minestom.extras.appleskin;

import net.uebliche.minstom.extras.common.Packet;
import net.uebliche.minstom.extras.common.PacketInfo;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import org.jetbrains.annotations.NotNull;

/**
 * Saturation Packet
 * @param saturation the saturation value
 */
@PacketInfo(channel = "appleskin:saturation")
public record SaturationPacket(
        float saturation
) implements Packet {

    public static final @NotNull NetworkBuffer.Type<SaturationPacket> SERIALIZER = NetworkBufferTemplate.template(
            NetworkBuffer.FLOAT, SaturationPacket::saturation,
            SaturationPacket::new
    );

}
