package net.uebliche.minestom.extras.simplevoicechat.network.clientbound;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.uebliche.minstom.extras.common.Packet;
import net.uebliche.minstom.extras.common.PacketInfo;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@PacketInfo(channel = "voicechat:remove_group")
public record GroupRemovedPacket(
        UUID group
) implements Packet {

    public static final @NotNull NetworkBuffer.Type<GroupRemovedPacket> SERIALIZER = NetworkBufferTemplate.template(
            NetworkBuffer.UUID, GroupRemovedPacket::group,
            GroupRemovedPacket::new
    );

}
