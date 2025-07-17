package net.uebliche.minestom.extras.simplevoicechat;

import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.instance.AddEntityToInstanceEvent;
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.tag.Tag;
import net.uebliche.minestom.extras.simplevoicechat.data.VoiceState;
import net.uebliche.minestom.extras.simplevoicechat.network.clientbound.*;
import net.uebliche.minestom.extras.simplevoicechat.network.serverbound.*;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.VoiceGroup;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.VoiceServer;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.VoicePacketBuffer;
import net.uebliche.minstom.extras.common.Extra;
import net.uebliche.minstom.extras.common.ExtraRegistry;
import net.uebliche.minstom.extras.common.Packet;
import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;
import java.util.UUID;
import java.util.function.Consumer;

public class SimpleVoiceChatExtra extends Extra {


    public static final @NotNull Tag<SimpleVoiceChatExtra> INSTANCE_TAG = Tag.Transient("simplevoicechat:instance");

    public final @NotNull Tag<SocketAddress> SOCKET_ADDRESS_TAG = Tag.Transient("simplevoicechat:socketAddress:" + UUID.randomUUID());
    public final @NotNull Tag<Long> LAST_KEEP_ALIVE_TAG = Tag.Transient("simplevoicechat:lastKeepAlive:" + UUID.randomUUID());
    public final @NotNull Tag<UUID> SECRET_TAG = Tag.Transient("simplevoicechat:secret:" + UUID.randomUUID());
    public final @NotNull Tag<VoicePacketBuffer> PUFFER_TAG = Tag.Transient("simplevoicechat:puffer:" + UUID.randomUUID());
    public final @NotNull Tag<VoiceGroup> GROUP_TAG = Tag.Transient("simplevoicechat:group:" + UUID.randomUUID());
    public final @NotNull Tag<VoiceState> STATE_TAG = Tag.Transient("voicechat:player-state:" + UUID.randomUUID());
    /**
     * This tag is used to override the proximity distance for a player or instance.
     */
    public final @NotNull Tag<Double> PROXIMITY_DISTANCE_OVERRIDE_TAG = Tag.Transient("simplevoicechat:proximityDistanceOverride:" + UUID.randomUUID());

    private VoiceServer server;

    public SimpleVoiceChatExtra() {
        super();
    }

    public VoiceServer server() {
        return server;
    }

    private VoiceServer.Builder builder = new VoiceServer.Builder();

    @Override
    protected void onRegister(ExtraRegistry registry) {
        builder.onTimeout(player -> {
            player.kick("Timeout");
        });
        builder.publicAddress("192.168.178.100");
        server = builder.buildAndStart();
    }

    @Override
    protected void registerPluginMessagesTypes(Consumer<Class<? extends Packet>> packet) {
        // Clientbound
        packet.accept(CategoryAddedPacket.class);
        packet.accept(CategoryRemovedPacket.class);
        packet.accept(GroupCreatedPacket.class);
        packet.accept(GroupJoinedPacket.class);
        packet.accept(GroupRemovedPacket.class);
        packet.accept(PlayerStatePacket.class);
        packet.accept(PlayerStatesPacket.class);
        packet.accept(SecretPacket.class);

        // Serverbound
        packet.accept(GroupCreatePacket.class);
        packet.accept(GroupLeavePacket.class);
        packet.accept(GroupSetPacket.class);
        packet.accept(RequestSecretPacket.class);
        packet.accept(UpdateStatePacket.class);
    }

    @Override
    protected void registerDefaultPacketListerners(ExtraRegistry registry) {
        registry.setListener(GroupCreatePacket.class, this::onGroupCreate);
        registry.setListener(GroupLeavePacket.class, this::onGroupLeave);
        registry.setListener(GroupSetPacket.class, this::onGroupSet);
        registry.setListener(RequestSecretPacket.class, this::onRequestSecret);
        registry.setListener(UpdateStatePacket.class, this::onUpdateState);
    }


    private void onGroupCreate(GroupCreatePacket packet, Player player) {
        if (inInDifferentiationsRegistered(player))
            return;

    }

    private void onGroupLeave(GroupLeavePacket packet, Player player) {
        if (inInDifferentiationsRegistered(player))
            return;

    }

    private void onGroupSet(GroupSetPacket packet, Player player) {
        if (inInDifferentiationsRegistered(player))
            return;

    }


    private void onRequestSecret(RequestSecretPacket packet, Player player) {
        if (inInDifferentiationsRegistered(player))
            return;

        var secret = UUID.randomUUID();
        player.setTag(SECRET_TAG, secret);
        new SecretPacket(
                secret,
                builder.port(),
                player.getUuid(),
                builder.codec(),
                builder.mtu(),
                builder.proximityDistance(),
                (int) builder.keepAliveTimeout().toMillis(),
                builder.groupsEnabled(),
                builder.publicAddress(),
                builder.allowRecording()
        ).send(player);
    }

    private void onUpdateState(UpdateStatePacket packet, Player player) {
        if (inInDifferentiationsRegistered(player))
            return;
        var state = player.getAndUpdateTag(STATE_TAG, voiceState -> voiceState.setDisabled(packet.disabled()));
        broadcastToListeners(player, state.toPacket());
    }

    private void broadcastToListeners(Player player, @NotNull Packet packet) {

    }

    public boolean inInDifferentiationsRegistered(Player player) {
        if (player.getTag(INSTANCE_TAG) == null) {
            player.setTag(INSTANCE_TAG, this);
            return false;
        }
        return player.getTag(INSTANCE_TAG) != this;
    }

    @Override
    protected EventNode<InstanceEvent> eventNode() {
        return EventNode.type("minestom-extras:simplevoicechat", EventFilter.INSTANCE)
                .addListener(PlayerSpawnEvent.class, event -> {

                })
                .addListener(AddEntityToInstanceEvent.class, event -> {
                    if (event.getEntity() instanceof Player player) {

                    }
                })
                .addListener(RemoveEntityFromInstanceEvent.class, event -> {
                    if (event.getEntity() instanceof Player player) {

                    }
                })


                ;
    }


}
