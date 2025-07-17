package net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.serverbound;

import net.minestom.server.network.NetworkBuffer;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.SVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.SoundSVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.VoicePacketInfo;
import org.jetbrains.annotations.NotNull;

@VoicePacketInfo(id = 0x1, ttl = 500)
public class MicrophoneSVCPacket extends SoundSVCPacket<MicrophoneSVCPacket> implements SVCPacket<MicrophoneSVCPacket> {


    byte @NotNull [] data;
    long sequence;
    boolean whispering;

    public MicrophoneSVCPacket() {
    }

    @Override
    public MicrophoneSVCPacket read(NetworkBuffer buffer) {
        this.data = buffer.read(NetworkBuffer.BYTE_ARRAY);
        this.sequence = buffer.read(NetworkBuffer.LONG);
        this.whispering = buffer.read(NetworkBuffer.BOOLEAN);
        return this;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.write(NetworkBuffer.BYTE_ARRAY, data);
        buffer.write(NetworkBuffer.LONG, sequence);
        buffer.write(NetworkBuffer.BOOLEAN, whispering);
    }

    public byte @NotNull [] data() {
        return data;
    }

    public MicrophoneSVCPacket setData(byte @NotNull [] data) {
        this.data = data;
        return this;
    }

    public long sequence() {
        return sequence;
    }

    public MicrophoneSVCPacket setSequence(long sequence) {
        this.sequence = sequence;
        return this;
    }

    public boolean whispering() {
        return whispering;
    }

    public MicrophoneSVCPacket setWhispering(boolean whispering) {
        this.whispering = whispering;
        return this;
    }
}
