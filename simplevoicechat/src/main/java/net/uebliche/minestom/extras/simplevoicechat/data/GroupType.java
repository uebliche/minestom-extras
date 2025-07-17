package net.uebliche.minestom.extras.simplevoicechat.data;

import net.minestom.server.network.NetworkBuffer;
import org.jetbrains.annotations.NotNull;

public enum GroupType {

    NORMAL(true, false),
    OPEN(true, true),
    ISOLATED(false, false);


    private final boolean canHearNearbyPlayers;
    private final boolean canBeHeardByNearbyPlayers;

    GroupType(boolean canHearNearbyPlayers, boolean canBeHeardByNearbyPlayers) {
        this.canHearNearbyPlayers = canHearNearbyPlayers;
        this.canBeHeardByNearbyPlayers = canBeHeardByNearbyPlayers;
    }

    public boolean canHearNearbyPlayers() {
        return canHearNearbyPlayers;
    }

    public boolean canBeHeardByNearbyPlayers() {
        return canBeHeardByNearbyPlayers;
    }

    public static final NetworkBuffer.Type<GroupType> NETWORK_TYPE = new NetworkBuffer.Type<GroupType>() {
        @Override
        public void write(@NotNull NetworkBuffer buffer, GroupType value) {
            buffer.write(NetworkBuffer.SHORT, (short) value.ordinal());
        }

        @Override
        public GroupType read(@NotNull NetworkBuffer buffer) {
            short ordinal = buffer.read(NetworkBuffer.SHORT);
            GroupType[] values = GroupType.values();
            if (ordinal < 0 || ordinal >= values.length) {
                throw new IllegalArgumentException("Invalid VoiceGroupType ordinal: " + ordinal);
            }
            return values[ordinal];
        }
    };


}