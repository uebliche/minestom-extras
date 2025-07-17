package net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.serverbound;

import net.minestom.server.network.NetworkBuffer;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.SVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.VoicePacketInfo;

@VoicePacketInfo(id = 0x9)
public class ConnectionCheckSVCPacket implements SVCPacket<ConnectionCheckSVCPacket> {

    public static ConnectionCheckSVCPacket CONNECTION_CHECK = new ConnectionCheckSVCPacket();

    @Override
    public ConnectionCheckSVCPacket read(NetworkBuffer buffer) {
        return this;
    }

    @Override
    public void write(NetworkBuffer buffer) {

    }
}
