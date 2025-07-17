package net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.bidirectional;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.SVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.VoicePacketInfo;
import org.jetbrains.annotations.NotNull;

@VoicePacketInfo(id = 0x8)
public record KeepAliveSVCPacket() implements SVCPacket<KeepAliveSVCPacket> {

    public static KeepAliveSVCPacket KEEP_ALIVE = new KeepAliveSVCPacket();

    public static final @NotNull NetworkBuffer.Type<KeepAliveSVCPacket> SERIALIZER = NetworkBufferTemplate.template(
            KeepAliveSVCPacket::new
    );

    @Override
    public KeepAliveSVCPacket read(NetworkBuffer buffer) {
        return buffer.read(SERIALIZER);
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.write(SERIALIZER, this);
    }
}
