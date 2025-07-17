package net.uebliche.minestom.extras.simplevoicechat.voiceserver;

import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.RawPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;

public final class VoiceSocket implements AutoCloseable {

    private @Nullable DatagramSocket socket;
    private Logger log = LoggerFactory.getLogger(VoiceSocket.class);

    public void open(@NotNull InetAddress address, int port) throws SocketException {
        if (this.socket != null) throw new IllegalStateException("Socket already open");

        this.socket = new DatagramSocket(port, address);

        // https://datatracker.ietf.org/doc/html/rfc1349
        // setting this will allow the socket to prioritize reliability over speed
        this.socket.setTrafficClass(0x04);
    }

    public @NotNull RawPacket read() throws IOException {
        DatagramSocket socket = checkOpen();

        byte[] buffer = new byte[4096];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        long timestamp = System.currentTimeMillis();
        byte[] data = new byte[packet.getLength()];
        System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());

        return new RawPacket(data, packet.getSocketAddress(), timestamp);
    }

    public void write(byte @NotNull [] data, @NotNull SocketAddress address) throws IOException {
        checkOpen().send(new DatagramPacket(data, data.length, address));
    }

    @Override
    public void close() {
        if (this.socket == null || this.socket.isClosed()) return;
        this.socket.close();
        this.socket = null;
    }

    public boolean isClosed() {
        return this.socket == null || this.socket.isClosed();
    }

    public @NotNull DatagramSocket checkOpen() {
        if (socket == null || socket.isClosed()) throw new IllegalStateException("Socket not open");
        return socket;
    }

}
