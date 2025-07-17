package net.uebliche.minestom.extras.simplevoicechat.network.clientbound;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.uebliche.minstom.extras.common.Packet;
import net.uebliche.minstom.extras.common.PacketInfo;
import org.jetbrains.annotations.NotNull;

@PacketInfo(channel = "voicechat:remove_category")
public record CategoryRemovedPacket(
        String category
) implements Packet {

    public static final @NotNull NetworkBuffer.Type<CategoryRemovedPacket> SERIALIZER = NetworkBufferTemplate.template(
            NetworkBuffer.STRING, CategoryRemovedPacket::category,
            CategoryRemovedPacket::new
    );

}
