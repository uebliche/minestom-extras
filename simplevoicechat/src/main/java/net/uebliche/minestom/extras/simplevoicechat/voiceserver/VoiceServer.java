package net.uebliche.minestom.extras.simplevoicechat.voiceserver;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.instance.Instance;
import net.uebliche.minestom.extras.simplevoicechat.SimpleVoiceChatExtra;
import net.uebliche.minestom.extras.simplevoicechat.VoicePermissionValidator;
import net.uebliche.minestom.extras.simplevoicechat.data.Codec;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.RawPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.SVCPacket;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.bidirectional.KeepAliveSVCPacket;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class VoiceServer {

    private static final Logger log = LoggerFactory.getLogger(VoiceServer.class);


    private final Builder builder;


    private final @NotNull VoiceSocket socket = new VoiceSocket();

    private final @NotNull Map<SocketAddress, Player> connections = new HashMap<>();
    private final @NotNull BlockingQueue<RawPacket> packetQueue = new LinkedBlockingQueue<>();
    private final @NotNull VoiceHandler voiceHandler = new VoiceHandler();

    boolean running = false;

    protected VoiceServer(Builder builder) {
        this.builder = builder;
    }

    void start() {
        if (running) {
            throw new IllegalStateException("Voice server is already running");
        }
        running = true;
        // Start the voice server here
        Thread.ofVirtual().name("minestom-extras:simplevoicechat-entrypoint").start(this::entrypoint);
        Thread.ofVirtual().name("minestom-extras:simplevoicechat-processor").start(this::processor);
        log.info("Starting VoiceServer on {}:{}", builder.host(), builder.port());
    }


    private void entrypoint() {
        try {
            this.socket.open(this.builder.hostAddress(), this.builder.port());

            while (!this.socket.isClosed() || !this.running) {
                RawPacket packet = null;
                try {
                    packet = this.socket.read();
                    log.info("Received packet from {}", packet.address());
                    this.packetQueue.put(packet);
                } catch (IOException e) {
                    log.debug("Failed to read packet", e);
                } catch (InterruptedException e) {
                    log.debug("Interrupted", e);
                }
            }
        } catch (SocketException e) {
            log.warn("Failed to open socket", e);
        } finally {
            running = false;
        }
    }


    Long lastKeepAlive = 0L;
    private void processor() {
        while (running) {
            Long currentTime = System.currentTimeMillis();
            if(currentTime - lastKeepAlive > builder.keepAliveTimeout().toMillis()) {
                Map.copyOf(this.connections).entrySet().parallelStream().forEach(entry -> {
                    Optional.ofNullable(entry.getValue().getTag(SimpleVoiceChatExtra.INSTANCE_TAG)).ifPresent(extra -> {
                        if (currentTime - Optional.ofNullable(entry.getValue().getTag(extra.LAST_KEEP_ALIVE_TAG)).orElse(-1L) > builder.keepAliveTimeout().toMillis()) {
                            log.warn("Player {} is not responding, removing from connections", entry.getValue().getName());
                            entry.getValue().removeTag(extra.SOCKET_ADDRESS_TAG);
                            builder.onTimeout().accept(entry.getValue());
                            connections.remove(entry.getKey());
                        } else {
                            sendKeepAlive(entry.getValue());
                        }
                    });
                });
                lastKeepAlive = currentTime;
            }

            try {
                RawPacket packet = this.packetQueue.take();
                log.info("Processing packet from {}", packet.address());
                this.voiceHandler.read(packet);
            } catch (InterruptedException e) {
                log.debug("Interrupted", e);
            } catch (Exception e) {
                log.error("Failed to process packet", e);
            }

        }

    }


    private void sendKeepAlive(Player value) {
        write(value, KeepAliveSVCPacket.KEEP_ALIVE);
    }

    public <T extends SVCPacket<T>> void write(@NotNull Player player, @NotNull T packet) {
        retrieveSocketAddress(player).ifPresent(address -> {
            this.voiceHandler.write(player, packet).ifPresent(rawPacket -> {
                try {
                    this.socket.write(rawPacket, address);
                } catch (IOException e) {
                    log.debug("failed to write voice packet", e);
                }
            });
        });
    }

    private Optional<SocketAddress> retrieveSocketAddress(@NotNull Player player) {
        return Optional.ofNullable(player.getTag(SimpleVoiceChatExtra.INSTANCE_TAG)).map(extra -> player.getTag(extra.SOCKET_ADDRESS_TAG));
    }


    public void stop() {
        if (!running) {
            throw new IllegalStateException("Voice server is not running");
        }
        running = false;
        // Stop the voice server here
        log.info("Stopping VoiceServer on {}:{}", builder.host(), builder.port());
    }

    public final EventNode<Event> eventNode() {
        return EventNode.all("voice-server").addListener(PlayerDisconnectEvent.class, event -> {
            Player player = event.getPlayer();
            Optional.ofNullable(player.getTag(SimpleVoiceChatExtra.INSTANCE_TAG)).ifPresent(extra -> {
                connections.remove(player.getTag(extra.SOCKET_ADDRESS_TAG));
                player.removeTag(extra.SOCKET_ADDRESS_TAG);
            });
        });
    }

    public double proximityDistance(@NotNull Player player) {
        return Optional.ofNullable(player.getTag(SimpleVoiceChatExtra.INSTANCE_TAG)).map(extra -> Optional.ofNullable(player.getTag(extra.PROXIMITY_DISTANCE_OVERRIDE_TAG)).orElseGet(() -> player.getInstance().getTag(extra.PROXIMITY_DISTANCE_OVERRIDE_TAG))).orElse(builder.proximityDistance());
    }

    public void overrideProximityDistance(@NotNull Player player, double distance) {
        Optional.ofNullable(player.getTag(SimpleVoiceChatExtra.INSTANCE_TAG)).ifPresent(extra -> {
            player.setTag(extra.PROXIMITY_DISTANCE_OVERRIDE_TAG, distance);
        });
    }

    public void removeOverrideProximityDistance(@NotNull Player player) {
        Optional.ofNullable(player.getTag(SimpleVoiceChatExtra.INSTANCE_TAG)).ifPresent(extra -> {
            player.removeTag(extra.PROXIMITY_DISTANCE_OVERRIDE_TAG);
        });
    }

    public void setOverrideProximityDistance(@NotNull Instance instance, double distance) {
        Optional.ofNullable(instance.getTag(SimpleVoiceChatExtra.INSTANCE_TAG)).ifPresent(extra -> {
            instance.setTag(extra.PROXIMITY_DISTANCE_OVERRIDE_TAG, distance);
        });
    }

    public static class Builder {

        private Supplier<InetAddress> host = () -> {
            try {
                return InetAddress.getByName("0.0.0.0");
            } catch (UnknownHostException e) {
                log.warn("Failed to resolve host", e);
                return InetAddress.getLoopbackAddress();
            }
        };
        private int port = 25565;
        private Supplier<String> publicAddress = () -> {
            try {
                return new Scanner(new URL("https://checkip.amazonaws.com").openStream()).useDelimiter("\\A").next().trim();
            } catch (IOException e) {
                log.warn("Failed to read ip address", e);
                return null;
            }
        };
        private Duration keepAliveTimeout = Duration.ofSeconds(10);
        private Consumer<Player> onTimeout = player -> {
            player.kick("Voice server timeout");
        };
        private boolean groupsEnabled = true;
        private double proximityDistance = 24;
        private VoicePermissionValidator permissionValidator = VoicePermissionValidator.defaultValidator;

        private Codec codec = Codec.VOIP;
        private int mtu = 1024;
        private boolean allowRecording = false;


        public Builder host(String host) {
            this.host = () -> {
                try {
                    return InetAddress.getByName(host);
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
            };
            return this;
        }

        public Builder host(InetAddress host) {
            this.host = () -> host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder publicAddress(String publicAddress) {
            this.publicAddress = () -> publicAddress;
            return this;
        }

        public Builder publicAddress(Supplier<String> publicAddress) {
            this.publicAddress = publicAddress;
            return this;
        }

        public Builder keepAliveTimeout(Duration keepAliveTimeout) {
            this.keepAliveTimeout = keepAliveTimeout;
            return this;
        }

        public Builder onTimeout(Consumer<Player> onTimeout) {
            this.onTimeout = onTimeout;
            return this;
        }

        public Builder groupsEnabled(boolean groupsEnabled) {
            this.groupsEnabled = groupsEnabled;
            return this;
        }

        public Builder proximityDistance(double proximityDistance) {
            this.proximityDistance = proximityDistance;
            return this;
        }

        public Builder permissionValidator(VoicePermissionValidator permissionValidator) {
            this.permissionValidator = permissionValidator;
            return this;
        }

        public Builder codec(Codec codec) {
            this.codec = codec;
            return this;
        }

        public Builder mtu(int mtu) {
            this.mtu = mtu;
            return this;
        }

        public Builder allowRecording(boolean allowRecording) {
            this.allowRecording = allowRecording;
            return this;
        }

        public VoiceServer build() {
            return new VoiceServer(this);
        }

        public VoiceServer buildAndStart() {
            VoiceServer voiceServer = new VoiceServer(this);
            voiceServer.start();
            return voiceServer;
        }

        public String host() {
            return hostAddress().getHostName();
        }

        public InetAddress hostAddress() {
            if (host == null) {
                host = () -> {
                    try {
                        return InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        log.warn("Failed to get local host", e);
                        return InetAddress.getLoopbackAddress();
                    }
                };
            }
            return host.get();
        }

        public int port() {
            return port;
        }

        public Duration keepAliveTimeout() {
            return keepAliveTimeout;
        }

        public Consumer<Player> onTimeout() {
            return onTimeout;
        }

        public boolean groupsEnabled() {
            return groupsEnabled;
        }

        public double proximityDistance() {
            return proximityDistance;
        }

        public VoicePermissionValidator permissionValidator() {
            return permissionValidator;
        }

        public Codec codec() {
            return codec;
        }

        public int mtu() {
            return mtu;
        }

        public boolean allowRecording() {
            return allowRecording;
        }

        public String publicAddress() {
            return publicAddress.get();
        }
    }

}
