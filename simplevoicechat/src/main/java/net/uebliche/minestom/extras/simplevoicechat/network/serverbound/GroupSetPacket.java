package net.uebliche.minestom.extras.simplevoicechat.network.serverbound;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.uebliche.minstom.extras.common.Packet;
import net.uebliche.minstom.extras.common.PacketInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@PacketInfo(channel = "voicechat:set_group")
public record GroupSetPacket(
        @NotNull UUID group,
        @Nullable String password
) implements Packet {

    public static final @NotNull NetworkBuffer.Type<GroupSetPacket> SERIALIZER = NetworkBufferTemplate.template(
            NetworkBuffer.UUID, GroupSetPacket::group,
            NetworkBuffer.STRING.optional(), GroupSetPacket::password,
            GroupSetPacket::new
    );

}
