package net.uebliche.minestom.extras.simplevoicechat.voiceserver;

import net.minestom.server.Viewable;
import net.minestom.server.entity.Player;
import net.uebliche.minestom.extras.simplevoicechat.SimpleVoiceChatExtra;
import net.uebliche.minestom.extras.simplevoicechat.data.ClientGroup;
import net.uebliche.minestom.extras.simplevoicechat.data.GroupType;
import net.uebliche.minestom.extras.simplevoicechat.network.clientbound.GroupCreatedPacket;
import net.uebliche.minestom.extras.simplevoicechat.network.clientbound.GroupRemovedPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class VoiceGroup implements Viewable {

    private VoiceServer voiceServer;

    private final UUID id;
    private String name;

    private @Nullable String password;
    private boolean president;
    private boolean hidden;
    private GroupType type;

    private Set<Player> members;
    private Set<Player> viewers = new HashSet<>();

    GroupCreatedPacket createdPacket;
    GroupRemovedPacket removedPacket;


    public VoiceGroup(VoiceServer voiceServer, UUID id, String name, boolean president, Set<Player> members) {
        this.voiceServer = voiceServer;
        this.id = id;
        this.name = name;
        this.president = president;
        this.members = members;
        removedPacket = new GroupRemovedPacket(id);
    }

    private void updateGroupCreatedPacket() {
        createdPacket = new GroupCreatedPacket(new ClientGroup(id, name, password != null, president, hidden, type));
    }


    private void addMember(Player player) {
        Optional.ofNullable(player.getTag(SimpleVoiceChatExtra.INSTANCE_TAG)).ifPresent(extra -> {
            Optional.ofNullable(player.getTag(extra.GROUP_TAG)).ifPresent(voiceGroup -> {
                if (voiceGroup == this) return;
                voiceGroup.removeMember(player);
            });
            members.add(player);
            player.setTag(extra.GROUP_TAG, this);
            var newState = player.updateAndGetTag(extra.STATE_TAG, voiceState -> voiceState.setGroup(this.id));
            for (Player member : members) {
                newState.toPacket().send(member);
            }
            // TODO: send group update to all members
        });
    }

    private void removeMember(Player player) {
        Optional.ofNullable(player.getTag(SimpleVoiceChatExtra.INSTANCE_TAG)).ifPresent(extra -> {

            // TODO: check if player is in the group

            members.remove(player);
            // TODO: send group update to all members
        });
    }

    public Set<Player> members() {
        return members;
    }

    @Override
    public boolean addViewer(@NotNull Player player) {
        if (viewers.contains(player)) return false;
        viewers.add(player);

        return true;
    }

    @Override
    public boolean removeViewer(@NotNull Player player) {
        return false;
    }

    @Override
    public @NotNull Set<@NotNull Player> getViewers() {
        return Set.of();
    }

    public GroupType type() {
        return type;
    }
}
