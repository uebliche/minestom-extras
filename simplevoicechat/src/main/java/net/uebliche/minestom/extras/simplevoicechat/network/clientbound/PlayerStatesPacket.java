package net.uebliche.minestom.extras.simplevoicechat.network.clientbound;


import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.uebliche.minestom.extras.simplevoicechat.data.VoiceState;
import net.uebliche.minstom.extras.common.Packet;
import net.uebliche.minstom.extras.common.PacketInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@PacketInfo(channel = "voicechat:player_states")
public record PlayerStatesPacket(@NotNull Collection<VoiceState> states) implements Packet {

    public static final @NotNull NetworkBuffer.Type<PlayerStatesPacket> SERIALIZER = NetworkBufferTemplate.template(
            VoiceState.NETWORK_LIST_TYPE, PlayerStatesPacket::states,
            PlayerStatesPacket::new
    );

}
