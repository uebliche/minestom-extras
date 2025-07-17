package net.uebliche.minestom.extras.simplevoicechat.voiceserver;

import net.minestom.server.entity.Player;
import net.uebliche.minestom.extras.simplevoicechat.SimpleVoiceChatExtra;
import net.uebliche.minestom.extras.simplevoicechat.voiceserver.network.serverbound.MicrophoneSVCPacket;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class VoicePlayerProcessor {

    private final BlockingQueue<MicrophoneSVCPacket> queue = new LinkedBlockingQueue<>();
    private final VoiceServer voiceServer;
    private final Player sender;

    public VoicePlayerProcessor(VoiceServer voiceServer, Player sender) {
        this.voiceServer = voiceServer;
        this.sender = sender;
        start();
    }

    public void enqueue(MicrophoneSVCPacket packet) {
        queue.add(packet);
    }

    Set<Player> getListeners() {
        Set<Player> set = Set.of();
        double distance = voiceServer.proximityDistance(sender);
        sender.getInstance().getNearbyEntities(sender.getPosition(), 10);
        return set;
    }

    public void start() {
        Thread.ofVirtual().start(() -> {
            while (true) {
                MicrophoneSVCPacket packet = queue.poll();
                Optional.ofNullable(sender.getTag(SimpleVoiceChatExtra.INSTANCE_TAG)).ifPresent(extra -> {
                    Optional.ofNullable(sender.getTag(extra.GROUP_TAG)).ifPresent(voiceGroup -> {
                        if (voiceGroup.type().canBeHeardByNearbyPlayers()) {
                            Set<Player> nearbyPlayers = getListeners();

                        }
                        voiceGroup.members().forEach(member -> {

                        });
                    });
                });
            }
        });
    }

}
