package net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.serverbound;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.SVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.VoicePacketInfo;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@VoicePacketInfo(id = 0x5)
public record AuthenticateSVCPacket(
        @NotNull UUID player,
        @NotNull UUID secret
) implements SVCPacket<AuthenticateSVCPacket> {

    public static final @NotNull NetworkBuffer.Type<AuthenticateSVCPacket> SERIALIZER = NetworkBufferTemplate.template(
            NetworkBuffer.UUID, AuthenticateSVCPacket::player,
            NetworkBuffer.UUID, AuthenticateSVCPacket::secret,
            AuthenticateSVCPacket::new
    );

    @Override
    public AuthenticateSVCPacket read(NetworkBuffer buffer) {
        return SERIALIZER.read(buffer);
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.write(SERIALIZER, this);
    }
}
