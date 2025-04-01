package net.uebliche.minestom.extras.viaversion;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.uebliche.minstom.extras.common.Packet;
import net.uebliche.minstom.extras.common.PacketInfo;
import net.minestom.server.network.NetworkBuffer;
import org.jetbrains.annotations.NotNull;

/**
 * PlayerDetailsPacket
 *
 * @param platformName    The name of the software/platform
 * @param platformVersion The version of the proxy/platform
 * @param version         The numeric protocol version of the player
 * @param versionName     The readable version name of the player's client
 */
@PacketInfo(channel = "vv:proxy_details")
public record PlayerDetailsPacket(
        @NotNull String platformName,
        @NotNull String platformVersion,
        int version,
        @NotNull String versionName
) implements Packet {

    private static final Gson GSON = new Gson();

    public static final @NotNull NetworkBuffer.Type<PlayerDetailsPacket> SERIALIZER = new NetworkBuffer.Type<>() {

        @Override
        public void write(@NotNull NetworkBuffer buffer, PlayerDetailsPacket value) {
            buffer.write(NetworkBuffer.RAW_BYTES, GSON.toJson(value).getBytes());
        }

        @Override
        public PlayerDetailsPacket read(@NotNull NetworkBuffer buffer) {
            final byte[] payloadBytes = buffer.read(NetworkBuffer.RAW_BYTES);
            final JsonObject payload = GSON.fromJson(new String(payloadBytes), JsonObject.class);

            return new PlayerDetailsPacket(
                    payload.get("platformName").getAsString(),
                    payload.get("platformVersion").getAsString(),
                    payload.get("version").getAsInt(),
                    payload.get("versionName").getAsString()
            );
        }
    };
}
