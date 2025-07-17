package net.uebliche.minestom.extras.simplevoicechat.data;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.NetworkBufferTemplate;
import net.uebliche.minestom.extras.simplevoicechat.network.clientbound.PlayerStatePacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class VoiceState {

    public static final @NotNull NetworkBuffer.Type<VoiceState> NETWORK_TYPE = NetworkBufferTemplate.template(
            NetworkBuffer.BOOLEAN, VoiceState::disabled,
            NetworkBuffer.BOOLEAN, VoiceState::disconnected,
            NetworkBuffer.UUID, VoiceState::uuid,
            NetworkBuffer.STRING, VoiceState::name,
            NetworkBuffer.OPT_UUID, VoiceState::group,
            VoiceState::new
    );

    public static final @NotNull NetworkBuffer.Type<Collection<VoiceState>> NETWORK_LIST_TYPE = new NetworkBuffer.Type<>() {
        @Override
        public void write(@NotNull NetworkBuffer buffer, Collection<VoiceState> value) {
            buffer.write(NetworkBuffer.INT, value.size());
            for (VoiceState state : value) buffer.write(NETWORK_TYPE, state);
        }

        @Override
        public Collection<VoiceState> read(@NotNull NetworkBuffer buffer) {
            int size = buffer.read(NetworkBuffer.INT);
            List<VoiceState> list = new ArrayList<>(size);
            for (int i = 0; i < size; i++) list.add(buffer.read(NETWORK_TYPE));
            return list;
        }
    };

    private PlayerStatePacket packet;
    private boolean disabled;
    private boolean disconnected;
    private final @NotNull UUID uuid;
    private final @NotNull String name;
    @Nullable
    private UUID group;

    public VoiceState(
            boolean disabled,
            boolean disconnected,
            @NotNull UUID uuid,
            @NotNull String name,
            @Nullable UUID group
    ) {
        this.disabled = disabled;
        this.disconnected = disconnected;
        this.uuid = uuid;
        this.name = name;
        this.group = group;
    }

    public boolean disabled() {
        return disabled;
    }

    public boolean disconnected() {
        return disconnected;
    }

    public @NotNull UUID uuid() {
        return uuid;
    }

    public @NotNull String name() {
        return name;
    }

    @Nullable
    public UUID group() {
        return group;
    }

    public VoiceState setDisabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public VoiceState setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
        return this;
    }

    public VoiceState setGroup(@Nullable UUID group) {
        this.group = group;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (VoiceState) obj;
        return this.disabled == that.disabled &&
                this.disconnected == that.disconnected &&
                Objects.equals(this.uuid, that.uuid) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.group, that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(disabled, disconnected, uuid, name, group);
    }

    @Override
    public String toString() {
        return "VoiceState[" +
                "disabled=" + disabled + ", " +
                "disconnected=" + disconnected + ", " +
                "uuid=" + uuid + ", " +
                "name=" + name + ", " +
                "group=" + group + ']';
    }

    public @NotNull PlayerStatePacket toPacket() {
        return packet.setState(this);
    }
}
