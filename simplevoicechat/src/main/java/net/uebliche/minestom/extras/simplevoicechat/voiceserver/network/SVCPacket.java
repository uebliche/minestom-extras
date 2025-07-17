package net.uebliche.minestom.extras.simplevoicechat.voiceserver.network;

import net.minestom.server.network.NetworkBuffer;

public interface SVCPacket<T extends SVCPacket<T>> {

    T read(NetworkBuffer buffer);

    void write(NetworkBuffer buffer);


}
