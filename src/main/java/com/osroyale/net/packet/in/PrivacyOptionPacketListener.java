package com.osroyale.net.packet.in;

import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.ClientPackets;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;

@PacketListenerMeta({ClientPackets.PRIVACY_OPTIONS})
public final class PrivacyOptionPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final int publicMode = packet.readByte();
        final int privateMode = packet.readByte();
        final int tradeMode = packet.readByte();
        final int clanMode = packet.readByte();
        player.relations.setPrivacyChatModes(publicMode, privateMode, clanMode, tradeMode);
    }

}
