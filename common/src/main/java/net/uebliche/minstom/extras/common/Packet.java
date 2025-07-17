package net.uebliche.minstom.extras.common;

import net.minestom.server.entity.Player;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.packet.server.common.PluginMessagePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * This interface is used to mark classes as packets.
 */
public interface Packet {

    static Logger log = LoggerFactory.getLogger(Packet.class);

    default <T> PluginMessagePacket toPluginMessage() {
        String channel = getClass().getAnnotation(PacketInfo.class).channel();
        try {
            Field serializerField = getClass().getField("SERIALIZER");
            NetworkBuffer.Type<T> serializer = (NetworkBuffer.Type<T>) serializerField.get(null);
            NetworkBuffer buffer = NetworkBuffer.resizableBuffer();
            T instance = (T) this;
            buffer.write(serializer, instance);
            byte[] data = new byte[(int) buffer.writeIndex()];
            buffer.copyTo(0, data, 0, data.length);
            return new PluginMessagePacket(channel, data);
        } catch (NoSuchFieldException e) {
            log.warn("Could not find SERIALIZER field in Packet class " + getClass().getName());
            return null;
        } catch (IllegalAccessException e) {
            log.warn("Could not access SERIALIZER field in Packet class " + getClass().getName());
            return null;
        }
    }

    default void send(Player player) {
        player.sendPacket(toPluginMessage());
    }
}
