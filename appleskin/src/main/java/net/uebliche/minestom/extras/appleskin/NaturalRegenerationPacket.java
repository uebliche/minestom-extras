package net.uebliche.minestom.extras.appleskin;

import net.uebliche.minstom.extras.common.Packet;
import net.uebliche.minstom.extras.common.PacketInfo;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import org.jetbrains.annotations.NotNull;

/**
 * Packet to set the natural regeneration state.
 * @param naturalRegeneration Whether natural regeneration is enabled or not.
 */
@PacketInfo(channel = "appleskin:natural_regeneration")
public record NaturalRegenerationPacket(
        boolean naturalRegeneration
) implements Packet {

    public static final @NotNull NetworkBuffer.Type<NaturalRegenerationPacket> SERIALIZER = NetworkBufferTemplate.template(
            NetworkBuffer.BOOLEAN, NaturalRegenerationPacket::naturalRegeneration,
            NaturalRegenerationPacket::new
    );

}
