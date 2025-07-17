package net.uebliche.minestom.extras.simplevoicechat.network.clientbound;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.uebliche.minestom.extras.simplevoicechat.data.VoiceState;
import net.uebliche.minstom.extras.common.Packet;
import net.uebliche.minstom.extras.common.PacketInfo;
import org.jetbrains.annotations.NotNull;

@PacketInfo(channel = "voicechat:player_state")
public class PlayerStatePacket implements Packet {

    private @NotNull VoiceState state;

    public static final @NotNull NetworkBuffer.Type<PlayerStatePacket> SERIALIZER = NetworkBufferTemplate.template(
            VoiceState.NETWORK_TYPE, PlayerStatePacket::state,
            PlayerStatePacket::new
    );

    public PlayerStatePacket(@NotNull VoiceState state) {
        this.state = state;
    }

    public @NotNull VoiceState state() {
        return state;
    }

    public PlayerStatePacket setState(@NotNull VoiceState state) {
        this.state = state;
        return this;
    }
}
