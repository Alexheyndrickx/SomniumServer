package com.osroyale.net.packet.in;

import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.util.Utility;

/**
 * The {@link GamePacket} responsible for reciving a string sent by the
 * client.
 * 
 * @author Michael | Chex
 */
@PacketListenerMeta(60)
public class InputStringPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		String input = Utility.longToString(packet.readLong()).replace("_", " ");
	
		if (player.enterInputListener.isPresent()) {
			player.enterInputListener.get().accept(input);
//			player.enterInputListener = Optional.empty();
		}
	}
}