package net.uebliche.minestom.extras.simplevoicechat.network.serverbound;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.uebliche.minestom.extras.simplevoicechat.data.GroupType;
import net.uebliche.minstom.extras.common.Packet;
import net.uebliche.minstom.extras.common.PacketInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@PacketInfo(channel = "voicechat:create_group")
public record GroupCreatePacket(
        @NotNull String name,
        @Nullable String password,
        @NotNull GroupType type
) implements Packet {

    public static final @NotNull NetworkBuffer.Type<GroupCreatePacket> SERIALIZER = NetworkBufferTemplate.template(
            NetworkBuffer.STRING, GroupCreatePacket::name,
            NetworkBuffer.STRING.optional(), GroupCreatePacket::password,
            GroupType.NETWORK_TYPE, GroupCreatePacket::type,
            GroupCreatePacket::new
    );

}
