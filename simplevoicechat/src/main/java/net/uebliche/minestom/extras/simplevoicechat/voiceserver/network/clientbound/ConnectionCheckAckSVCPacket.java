package net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.clientbound;

import net.minestom.server.network.NetworkBuffer;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.SVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.VoicePacketInfo;

@VoicePacketInfo(id = 0xA)
public record ConnectionCheckAckSVCPacket() implements SVCPacket<ConnectionCheckAckSVCPacket> {

    public static ConnectionCheckAckSVCPacket CONNECTION_CHECK_ACK = new ConnectionCheckAckSVCPacket();


    @Override
    public ConnectionCheckAckSVCPacket read(NetworkBuffer buffer) {
        return this;
    }

    @Override
    public void write(NetworkBuffer buffer) {

    }
}
