package net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.clientbound;

import net.minestom.server.network.NetworkBuffer;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.SoundSVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.VoicePacketInfo;

import java.util.Optional;

@VoicePacketInfo(id = 0x03)
public class GroupSoundSVCPacket extends SoundSVCPacket<GroupSoundSVCPacket> {


    public GroupSoundSVCPacket() {
        super();
    }

    @Override
    public GroupSoundSVCPacket read(NetworkBuffer buffer) {
        this.channelId = buffer.read(NetworkBuffer.UUID);
        this.sender = buffer.read(NetworkBuffer.UUID);
        this.data = buffer.read(NetworkBuffer.BYTE_ARRAY);
        this.sequenceNumber = buffer.read(NetworkBuffer.LONG);

        byte data = buffer.read(NetworkBuffer.BYTE);
        if ((data & HAS_CATEGORY) == HAS_CATEGORY) {
            this.category = buffer.read(NetworkBuffer.STRING_IO_UTF8);
        }
        return this;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.write(NetworkBuffer.UUID, this.channelId);
        buffer.write(NetworkBuffer.UUID, this.sender);
        buffer.write(NetworkBuffer.BYTE_ARRAY, this.data);
        buffer.write(NetworkBuffer.LONG, this.sequenceNumber);

        Optional.ofNullable(this.category).ifPresentOrElse(category -> {
            buffer.write(NetworkBuffer.BYTE, HAS_CATEGORY);
            buffer.write(NetworkBuffer.STRING_IO_UTF8, this.category);
        }, () -> {
            buffer.write(NetworkBuffer.BYTE, HAS_NO_DATA);
        });

    }
}
