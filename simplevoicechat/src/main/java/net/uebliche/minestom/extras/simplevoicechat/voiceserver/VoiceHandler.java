package net.uebliche.minestom.extras.simplevoicechat.voiceserver;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.network.NetworkBuffer;
import net.uebliche.minestom.extras.simplevoicechat.SimpleVoiceChatExtra;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.RawPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.SVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.VoicePacketInfo;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.bidirectional.KeepAliveSVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.clientbound.*;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.serverbound.AuthenticateSVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.serverbound.ConnectionCheckSVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.serverbound.MicrophoneSVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.serverbound.PingSVCPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

public class VoiceHandler {

    private static final byte MAGIC_BYTE = (byte) 0b11111111;

    private final Map<Integer, Class<? extends SVCPacket<?>>> idToPacket = new HashMap<>();
    private final Map<Class<? extends SVCPacket<?>>, BiConsumer<Player, NetworkBuffer>> handlers = new HashMap<>();

    public VoiceHandler() {
        this.register(MicrophoneSVCPacket.class, this::onMicrophone);
        this.register(PlayerSoundSVCPacket.class);
        this.register(GroupSoundSVCPacket.class);
        this.register(PositionedSoundSVCPacket.class);
        this.register(AuthenticateSVCPacket.class);
        this.register(AuthenticationAcknowledgedSVCPacket.class);
        this.register(PingSVCPacket.class, this::onPing);
        this.register(KeepAliveSVCPacket.class, this::onKeepAlive);
        this.register(ConnectionCheckSVCPacket.class, this::onConnectionCheck);
        this.register(ConnectionCheckAckSVCPacket.class);

    }


    private <T extends SVCPacket<T>> void register(Class<T> packetClass) {
        if (packetClass.isAnnotationPresent(VoicePacketInfo.class)) {
            var info = packetClass.getAnnotation(VoicePacketInfo.class);
            this.idToPacket.put(info.id(), packetClass);
        }
    }

    private <T extends SVCPacket<T>> void register(Class<T> packetClass, BiConsumer<Player, NetworkBuffer> handler) {
        if (packetClass.isAnnotationPresent(VoicePacketInfo.class)) {
            var info = packetClass.getAnnotation(VoicePacketInfo.class);
            this.idToPacket.put(info.id(), packetClass);
            this.handlers.put(packetClass, handler);
        }
    }

    public @Nullable void read(@NotNull RawPacket packet) throws Exception {
        byte[] data = packet.data();
        NetworkBuffer outer = NetworkBuffer.wrap(data, 0, data.length);

        if (outer.read(NetworkBuffer.BYTE) != MAGIC_BYTE) throw new IllegalStateException("invalid magic byte");

        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(outer.read(NetworkBuffer.UUID));
        if (player == null || !player.isOnline()) return; // player has disconnected
        Optional.ofNullable(player.getTag(SimpleVoiceChatExtra.INSTANCE_TAG)).ifPresent(extra -> {
            UUID secret = player.getTag(extra.SECRET_TAG);
            if (secret == null) throw new IllegalStateException("no secret for player");
            AES.decrypt(secret, outer.read(NetworkBuffer.BYTE_ARRAY)).ifPresent(decrypted -> {
                NetworkBuffer buffer = NetworkBuffer.wrap(decrypted, 0, decrypted.length);
                int id = buffer.read(NetworkBuffer.BYTE);
                var packetClass = this.idToPacket.get(id);
                if (packetClass == null) throw new IllegalStateException("invalid packet id");
                Optional.ofNullable(handlers.get(packetClass)).ifPresent(handler -> handler.accept(player, buffer));
            });

        });

    }

    public <T extends SVCPacket<T>> Optional<byte[]> write(@NotNull Player player, @NotNull T packet) {
        NetworkBuffer buffer = NetworkBuffer.resizableBuffer();
        buffer.write(NetworkBuffer.BYTE, MAGIC_BYTE);
        return Optional.ofNullable(player.getTag(SimpleVoiceChatExtra.INSTANCE_TAG)).map(extra -> {

            UUID secret = player.getTag(extra.SECRET_TAG);
            if (secret == null) throw new IllegalStateException("no secret for player");

            NetworkBuffer inner = NetworkBuffer.resizableBuffer();
            int id = packet.getClass().getAnnotation(VoicePacketInfo.class).id();
            inner.write(NetworkBuffer.BYTE, (byte) id);
            packet.write(inner);

            byte[] data = new byte[(int) inner.writeIndex()];
            inner.copyTo(0, data, 0, data.length);

            return AES.encrypt(secret, data).map(encrypted -> {
                buffer.write(NetworkBuffer.BYTE_ARRAY, encrypted);

                byte[] result = new byte[(int) buffer.writeIndex()];
                buffer.copyTo(0, result, 0, result.length);
                return result;
            }).orElse(null);
        });

    }

    private void onMicrophone(Player player, NetworkBuffer microphoneSVCPacket) {
        Optional.ofNullable(player.getTag(SimpleVoiceChatExtra.INSTANCE_TAG)).ifPresent(extra -> {
            player.getTag(extra.PUFFER_TAG).push(packetClass, buffer);
        });
    }

    private void onConnectionCheck(Player player, NetworkBuffer connectionCheckSVCPacket) {

        Optional.ofNullable(player.getTag(SimpleVoiceChatExtra.INSTANCE_TAG)).ifPresent(extra -> {

        });
    }

    private void onKeepAlive(Player player, NetworkBuffer keepAliveSVCPacket) {

        Optional.ofNullable(player.getTag(SimpleVoiceChatExtra.INSTANCE_TAG)).ifPresent(extra -> {

        });
    }

    private void onPing(Player player, NetworkBuffer pingSVCPacket) {

        Optional.ofNullable(player.getTag(SimpleVoiceChatExtra.INSTANCE_TAG)).ifPresent(extra -> {

        });
    }

}
