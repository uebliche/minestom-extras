package net.uebliche.minestom.extras.simplevoicechat.network.serverbound;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.uebliche.minstom.extras.common.Packet;
import net.uebliche.minstom.extras.common.PacketInfo;
import org.jetbrains.annotations.NotNull;

@PacketInfo(channel = "voicechat:leave_group")
public record GroupLeavePacket() implements Packet {

    public static final @NotNull NetworkBuffer.Type<GroupLeavePacket> SERIALIZER = NetworkBufferTemplate.template(
            GroupLeavePacket::new
    );

}
