package net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.clientbound;

import net.minestom.server.coordinate.Point;
import net.minestom.server.network.NetworkBuffer;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.SoundSVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.VoicePacketInfo;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@VoicePacketInfo(id = 0x4)
public class PositionedSoundSVCPacket extends SoundSVCPacket<PositionedSoundSVCPacket> {

    private Point position;
    private float distance;

    public PositionedSoundSVCPacket(UUID channelId, UUID sender, byte[] data, long sequenceNumber, @Nullable String category, Point position, float distance) {
        super(channelId, sender, data, sequenceNumber, category);
        this.position = position;
        this.distance = distance;
    }

    public PositionedSoundSVCPacket() {
    }

    public PositionedSoundSVCPacket setDistance(float distance) {
        this.distance = distance;
        return this;
    }

    public PositionedSoundSVCPacket setPosition(Point position) {
        this.position = position;
        return this;
    }

    public float distance() {
        return distance;
    }

    public Point position() {
        return position;
    }

    @Override
    public PositionedSoundSVCPacket read(NetworkBuffer buffer) {
        this.channelId = buffer.read(NetworkBuffer.UUID);
        this.sender = buffer.read(NetworkBuffer.UUID);
        this.position = buffer.read(NetworkBuffer.VECTOR3D);
        this.data = buffer.read(NetworkBuffer.BYTE_ARRAY);
        this.sequenceNumber = buffer.read(NetworkBuffer.LONG);
        this.distance = buffer.read(NetworkBuffer.FLOAT);

        byte data = buffer.read(NetworkBuffer.BYTE);
        if ((data & HAS_CATEGORY) != 0) {
            this.category = buffer.read(NetworkBuffer.STRING);
        }

        return this;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.write(NetworkBuffer.UUID, channelId);
        buffer.write(NetworkBuffer.UUID, sender);
        buffer.write(NetworkBuffer.VECTOR3D, position);
        buffer.write(NetworkBuffer.BYTE_ARRAY, data);
        buffer.write(NetworkBuffer.LONG, sequenceNumber);
        buffer.write(NetworkBuffer.FLOAT, distance);
        byte data = 0;
        if (category != null) {
            data |= HAS_CATEGORY;
            buffer.write(NetworkBuffer.STRING, category);
        }
        buffer.write(NetworkBuffer.BYTE, data);
    }
}
