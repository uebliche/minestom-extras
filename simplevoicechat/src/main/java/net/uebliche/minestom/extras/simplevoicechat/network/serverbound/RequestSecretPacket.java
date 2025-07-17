package net.uebliche.minestom.extras.simplevoicechat.network.serverbound;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.uebliche.minstom.extras.common.Packet;
import net.uebliche.minstom.extras.common.PacketInfo;
import org.jetbrains.annotations.NotNull;

@PacketInfo(channel = "voicechat:request_secret")
public record RequestSecretPacket(int version) implements Packet {

    public static final @NotNull NetworkBuffer.Type<RequestSecretPacket> SERIALIZER = NetworkBufferTemplate.template(
            NetworkBuffer.INT, RequestSecretPacket::version,
            RequestSecretPacket::new
    );

}