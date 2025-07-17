package net.uebliche.minestom.extras.simplevoicechat.network.clientbound;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.uebliche.minestom.extras.simplevoicechat.data.Category;
import net.uebliche.minstom.extras.common.Packet;
import net.uebliche.minstom.extras.common.PacketInfo;
import org.jetbrains.annotations.NotNull;

@PacketInfo(channel = "voicechat:add_category")
public record CategoryAddedPacket(
        @NotNull String identifier,
        @NotNull Category category
) implements Packet {

    public static final @NotNull NetworkBuffer.Type<CategoryAddedPacket> SERIALIZER = NetworkBufferTemplate.template(
            NetworkBuffer.STRING, CategoryAddedPacket::identifier,
            Category.NETWORK_TYPE, CategoryAddedPacket::category,
            CategoryAddedPacket::new
    );

}
