package net.uebliche.minestom.extras.simplevoicechat;

import net.minestom.server.entity.Player;

public interface VoicePermissionValidator {

    boolean canSpeak(Player player);

    boolean canHear(Player player);

    boolean canCreateGroup(Player player);

    boolean canJoinGroup(Player player);

    boolean canLeaveGroup(Player player);

    static final VoicePermissionValidator defaultValidator = new VoicePermissionValidator() {
        @Override
        public boolean canSpeak(Player player) {
            return true;
        }

        @Override
        public boolean canHear(Player player) {
            return true;
        }

        @Override
        public boolean canCreateGroup(Player player) {
            return true;
        }

        @Override
        public boolean canJoinGroup(Player player) {
            return true;
        }

        @Override
        public boolean canLeaveGroup(Player player) {
            return true;
        }
    };
}
