package net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.clientbound;

import net.minestom.server.network.NetworkBuffer;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.SoundSVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.VoicePacketInfo;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@VoicePacketInfo(id = 0x2)
public class PlayerSoundSVCPacket extends SoundSVCPacket<PlayerSoundSVCPacket> {

    private float distance;
    private boolean whispering;

    public PlayerSoundSVCPacket(UUID channelId, UUID sender, byte[] data, long sequenceNumber, @Nullable String category, float distance, boolean whispering) {
        super(channelId, sender, data, sequenceNumber, category);
        this.distance = distance;
        this.whispering = whispering;
    }


    public PlayerSoundSVCPacket setDistance(float distance) {
        this.distance = distance;
        return this;
    }

    public float distance() {
        return distance;
    }

    public PlayerSoundSVCPacket setWhispering(boolean whispering) {
        this.whispering = whispering;
        return this;
    }

    public boolean whispering() {
        return whispering;
    }

    @Override
    public PlayerSoundSVCPacket read(NetworkBuffer buffer) {
        this.channelId = buffer.read(NetworkBuffer.UUID);
        this.sender = buffer.read(NetworkBuffer.UUID);
        this.data = buffer.read(NetworkBuffer.BYTE_ARRAY);
        this.sequenceNumber = buffer.read(NetworkBuffer.LONG);
        this.distance = buffer.read(NetworkBuffer.FLOAT);
        byte flags = buffer.read(NetworkBuffer.BYTE);
        this.whispering = (flags & IS_WHISPERING) != 0;
        if ((flags & HAS_CATEGORY_AND_IS_WHISPERING) != 0) {
            this.category = buffer.read(NetworkBuffer.STRING);
        } else {
            this.category = null;
        }
        return this;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.write(NetworkBuffer.UUID, channelId);
        buffer.write(NetworkBuffer.UUID, sender);
        buffer.write(NetworkBuffer.BYTE_ARRAY, data);
        buffer.write(NetworkBuffer.LONG, sequenceNumber);
        buffer.write(NetworkBuffer.FLOAT, distance);
        buffer.write(NetworkBuffer.BYTE, (category != null ? (whispering ? HAS_CATEGORY_AND_IS_WHISPERING : IS_WHISPERING) : (whispering ? IS_WHISPERING : 0)));
        if (category != null) {
            buffer.write(NetworkBuffer.STRING, category);
        }

    }
}
