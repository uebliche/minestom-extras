package net.uebliche.minestom.extras.simplevoicechat.network.clientbound;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.uebliche.minestom.extras.simplevoicechat.data.ClientGroup;
import net.uebliche.minstom.extras.common.Packet;
import net.uebliche.minstom.extras.common.PacketInfo;
import org.jetbrains.annotations.NotNull;

@PacketInfo(channel = "voicechat:add_group")
public record GroupCreatedPacket(
        ClientGroup clientGroup
) implements Packet {

    public static final @NotNull NetworkBuffer.Type<GroupCreatedPacket> SERIALIZER = NetworkBufferTemplate.template(
            ClientGroup.NETWORK_TYPE, GroupCreatedPacket::clientGroup,
            GroupCreatedPacket::new
    );


}
