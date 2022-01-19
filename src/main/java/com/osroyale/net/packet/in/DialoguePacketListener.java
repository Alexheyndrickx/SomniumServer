package com.osroyale.net.packet.in;

import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.game.world.entity.mob.player.Player;

/**
 * The {@link GamePacket} responsible for dialogues.
 * 
 * @author Daniel | Obey
 */
@PacketListenerMeta(40)
public class DialoguePacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		player.dialogueFactory.execute();
	}
}
