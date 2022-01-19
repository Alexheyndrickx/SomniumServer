package com.osroyale.net.packet.out;

import com.osroyale.net.packet.OutgoingPacket;
import com.osroyale.net.packet.PacketType;
import com.osroyale.game.world.entity.mob.player.Player;

public class SendKillFeed extends OutgoingPacket {

    private final String killer;
    private final String victim;

    public SendKillFeed(String killer, String victim) {
        super(173, PacketType.VAR_BYTE);
        this.killer = killer;
        this.victim = victim;
    }

    @Override
    public boolean encode(Player player) {
        if (killer == null || killer.length() == 0 || victim == null || victim.length() == 0) {
            return false;
        }
        builder.writeString(killer)
        .writeString(victim);
        return true;
    }

}
