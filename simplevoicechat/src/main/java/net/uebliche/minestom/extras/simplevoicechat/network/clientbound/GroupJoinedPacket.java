package net.uebliche.minestom.extras.simplevoicechat.network.clientbound;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.uebliche.minstom.extras.common.Packet;
import net.uebliche.minstom.extras.common.PacketInfo;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@PacketInfo(channel = "voicechat:joined_group")
public record GroupJoinedPacket(
        UUID group,
        boolean incorrectPassword
) implements Packet {

    public static final @NotNull NetworkBuffer.Type<GroupJoinedPacket> SERIALIZER = NetworkBufferTemplate.template(
            NetworkBuffer.UUID.optional(), GroupJoinedPacket::group,
            NetworkBuffer.BOOLEAN, GroupJoinedPacket::incorrectPassword,
            GroupJoinedPacket::new
    );

}