package net.uebliche.minestom.extras.simplevoicechat.network.clientbound;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.uebliche.minestom.extras.simplevoicechat.data.Codec;
import net.uebliche.minstom.extras.common.Packet;
import net.uebliche.minstom.extras.common.PacketInfo;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@PacketInfo(channel = "voicechat:secret")
public record SecretPacket(
        @NotNull UUID secret,
        int port,
        @NotNull UUID player,
        @NotNull Codec codec,
        int mtu,
        double distance,
        int keepAlive,
        boolean groups,
        @NotNull String host,
        boolean recording
) implements Packet {

    public static final @NotNull NetworkBuffer.Type<SecretPacket> SERIALIZER = NetworkBufferTemplate.template(
            NetworkBuffer.UUID, SecretPacket::secret,
            NetworkBuffer.INT, SecretPacket::port,
            NetworkBuffer.UUID, SecretPacket::player,
            NetworkBuffer.Enum(Codec.class), SecretPacket::codec,
            NetworkBuffer.INT, SecretPacket::mtu,
            NetworkBuffer.DOUBLE, SecretPacket::distance,
            NetworkBuffer.INT, SecretPacket::keepAlive,
            NetworkBuffer.BOOLEAN, SecretPacket::groups,
            NetworkBuffer.STRING, SecretPacket::host,
            NetworkBuffer.BOOLEAN, SecretPacket::recording,
            SecretPacket::new
    );

}
