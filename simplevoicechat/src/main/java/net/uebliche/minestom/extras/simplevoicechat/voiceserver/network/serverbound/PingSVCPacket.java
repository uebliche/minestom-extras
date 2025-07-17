package net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.serverbound;

import net.minestom.server.network.NetworkBuffer;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.SVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.VoicePacketInfo;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@VoicePacketInfo(id = 0x7)
public class PingSVCPacket implements SVCPacket<PingSVCPacket> {

    @NotNull UUID player;
    long timestamp;

    @Override
    public PingSVCPacket read(NetworkBuffer buffer) {
        player = buffer.read(NetworkBuffer.UUID);
        timestamp = buffer.read(NetworkBuffer.LONG);
        return this;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.write(NetworkBuffer.UUID, player);
        buffer.write(NetworkBuffer.LONG, timestamp);
    }
}
