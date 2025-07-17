package net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.clientbound;

import net.minestom.server.network.NetworkBuffer;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.SVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.VoicePacketInfo;

@VoicePacketInfo(id = 0x6)
public record AuthenticationAcknowledgedSVCPacket() implements SVCPacket<AuthenticationAcknowledgedSVCPacket> {

    public static AuthenticationAcknowledgedSVCPacket AUTHENTICATION_ACKNOWLEDGED = new AuthenticationAcknowledgedSVCPacket();

    @Override
    public void write(NetworkBuffer buffer) {
    }

    @Override
    public AuthenticationAcknowledgedSVCPacket read(NetworkBuffer buffer) {
        return this;
    }
}
