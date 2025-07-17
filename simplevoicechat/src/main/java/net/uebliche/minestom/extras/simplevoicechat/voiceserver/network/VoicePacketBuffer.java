package net.uebliche.minestom.extras.simplevoicechat.voiceserver.network;

import net.minestom.server.entity.Player;
import net.minestom.server.network.NetworkBuffer;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.serverbound.MicrophoneSVCPacket;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class VoicePacketBuffer {

    private final Deque<MicrophoneSVCPacket> buffer = new ArrayDeque<>();
    private final Player player;
    private Thread thread;


    public VoicePacketBuffer(Player player) {
        this.player = player;
        this.thread = Thread.startVirtualThread(() -> {
            while (player != null && player.isOnline()) {
                Optional.of(buffer.removeFirst()).ifPresent(packet -> {

                });
            }
        });
    }

    public void push(@NotNull MicrophoneSVCPacket packet) {
        buffer.add(packet);
    }

}
