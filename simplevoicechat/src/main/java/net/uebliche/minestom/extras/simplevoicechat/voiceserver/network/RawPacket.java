package net.uebliche.minestom.extras.simplevoicechat.voiceserver.network;

import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;

public record RawPacket(
        byte @NotNull [] data,
        @NotNull SocketAddress address,
        long timestamp
) {
}
