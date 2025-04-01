package net.uebliche.minstom.extras.common;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerPluginMessageEvent;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.packet.server.common.PluginMessagePacket;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to register and handle custom packets in the Minestom server.
 */
public class ExtraRegistry {

    /**
     * This map is used to map classes to handlers.
     */
    protected final Map<Class<?>, Handler<?>> listeners = new HashMap<>();
    /**
     * This map is used to map classes to serializers.
     */
    protected final Map<Class<?>, NetworkBuffer.Type<?>> types = new HashMap<>();
    /**
     * This map is used to map classes to channels.
     */
    protected final Map<Class<?>, String> classToChannel = new HashMap<>();
    /**
     * This map is used to map channels to classes.
     */
    protected final Map<String, Class<?>> channelToClass = new HashMap<>();
    /**
     * This EventNode is used to handle plugin messages.
     */
    protected final EventNode<Event> rootNode = EventNode.all("extra")
            .addListener(PlayerPluginMessageEvent.class, event -> {
                Player player = event.getPlayer();
                String channel = event.getIdentifier();
                byte[] data = event.getMessage();

                Class<?> rawClass = channelToClass.get(channel);
                if (rawClass == null) return;

                NetworkBuffer.Type<?> rawType = types.get(rawClass);
                if (rawType == null) return;

                NetworkBuffer buffer = NetworkBuffer.wrap(data, 0, data.length);
                Object rawPacket = rawType.read(buffer);

                dispatch(rawClass, rawPacket, player);
            });

    /**
     * Creates a new ExtraRegistry instance.
     */
    public ExtraRegistry() {}

    /**
     * This method is used to write a packet to a PluginMessagePacket.
     *
     * @param packet The packet to write.
     * @param <T>    The type of the packet.
     * @return The PluginMessagePacket containing the data of the packet.
     */
    @SuppressWarnings("unchecked")
    <T extends Packet> @NotNull PluginMessagePacket write(@NotNull T packet) {
        NetworkBuffer.Type<T> serializer = (NetworkBuffer.Type<T>) types.get(packet.getClass());
        NetworkBuffer buffer = NetworkBuffer.resizableBuffer();
        buffer.write(serializer, packet);

        byte[] data = new byte[(int) buffer.writeIndex()];
        buffer.copyTo(0, data, 0, data.length);
        return new PluginMessagePacket(classToChannel.get(packet.getClass()), data);
    }

    /**
     * This method is used to get the EventNode for the ExtraRegistry.
     *
     * @return The EventNode for the ExtraRegistry.
     */
    public EventNode<Event> eventNode() {
        return rootNode;
    }

    /**
     * This method is used to dispatch a packet to the appropriate handler.
     *
     * @param clazz     The class of the packet.
     * @param rawPacket The raw packet to dispatch.
     * @param player    The player to dispatch the packet to.
     * @param <T>       The type of the packet.
     */
    @SuppressWarnings("unchecked")
    private <T extends Packet> void dispatch(Class<?> clazz, Object rawPacket, Player player) {
        Handler<T> handler = (Handler<T>) listeners.get(clazz);
        if (handler != null) {
            handler.accept((T) rawPacket, player);
        }
    }

    /**
     * This method is used to set a listener for a specific packet class.
     *
     * @param packetClass The class of the packet to listen for.
     * @param handler     The handler to call when the packet is received.
     * @param <T>         The type of the packet.
     */
    public <T extends Packet> void setListener(@NotNull Class<T> packetClass, @NotNull Handler<T> handler) {
        // Add Handler
        listeners.put(packetClass, handler);

        // Find and register SERIALIZER
        NetworkBuffer.Type<T> serializer = findSerializer(packetClass);
        types.put(packetClass, serializer);

        // Register Channel
        PacketInfo annotation = packetClass.getAnnotation(PacketInfo.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Packet class " + packetClass.getName() + " is not annotated with @PacketInfo");
        }
        classToChannel.put(packetClass, annotation.channel());
        channelToClass.put(annotation.channel(), packetClass);
    }

    /**
     * This method is used to find the serializer for a specific packet class.
     *
     * @param packetClass The class of the packet to find the serializer for.
     * @param <T>         The type of the packet.
     * @return The serializer for the packet class.
     */
    @SuppressWarnings("unchecked")
    private <T extends Packet> NetworkBuffer.Type<T> findSerializer(@NotNull Class<T> packetClass) {
        try {
            Field field = packetClass.getField("SERIALIZER");
            return (NetworkBuffer.Type<T>) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Missing public static final SERIALIZER field in " + packetClass.getName(), e);
        }
    }

    /**
     * This method is used to register a helper for the ExtraRegistry.
     */
    private final Map<Class<?>, Helper> helpers = new HashMap<>();

    /**
     * This method is used to register a helper for the ExtraRegistry.
     *
     * @param helper The helper to register.
     * @param <T>    The type of the helper.
     */
    @SuppressWarnings("unchecked")
    public <T extends Helper> void registerHelper(T helper) {
        Class<?> helperClass = helper.getClass();
        if (helpers.containsKey(helperClass)) {
            throw new IllegalArgumentException("Helper " + helperClass.getName() + " is already registered");
        }
        helpers.put(helperClass, helper);
        helper.registry = this;
        if (helper.eventNode() != null) {
            this.eventNode().addChild(helper.eventNode());
        }
    }

    /**
     * This method is used to get a helper for the ExtraRegistry.
     *
     * @param helperClass The class of the helper to get.
     * @param <T>         The type of the helper.
     * @return The helper for the ExtraRegistry.
     */
    @SuppressWarnings("unchecked")
    public <T extends Helper> T getHelper(Class<T> helperClass) {
        return (T) helpers.get(helperClass);
    }

}
