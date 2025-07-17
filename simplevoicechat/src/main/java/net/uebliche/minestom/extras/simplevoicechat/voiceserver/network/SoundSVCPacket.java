package net.uebliche.minestom.extras.simplevoicechat.voiceserver.network;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class SoundSVCPacket<T extends SoundSVCPacket<T>> implements SVCPacket<T> {

    //TODO: Make this a bit more readable and understandable
    public static final byte HAS_NO_DATA = 0b0;
    public static final byte IS_WHISPERING = 0b1;
    public static final byte HAS_CATEGORY = 0b10;
    public static final byte HAS_CATEGORY_AND_IS_WHISPERING = 0b11;

    protected UUID channelId;
    protected UUID sender;
    protected byte[] data;
    protected long sequenceNumber;
    @Nullable
    protected String category;

    public SoundSVCPacket(UUID channelId, UUID sender, byte[] data, long sequenceNumber, @Nullable String category) {
        this.channelId = channelId;
        this.sender = sender;
        this.data = data;
        this.sequenceNumber = sequenceNumber;
        this.category = category;
    }

    public SoundSVCPacket() {

    }

    public UUID getChannelId() {
        return channelId;
    }

    public UUID getSender() {
        return sender;
    }

    public byte[] getData() {
        return data;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    @Nullable
    public String getCategory() {
        return category;
    }


}
