package com.osroyale.net.packet.in;

import com.osroyale.game.world.InterfaceConstants;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.net.codec.ByteOrder;
import com.osroyale.net.packet.ClientPackets;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;

@PacketListenerMeta(ClientPackets.MOVE_ITEM)
public class MoveItemPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		final int interfaceId = packet.readShort(ByteOrder.LE, ByteModification.ADD);
		final int inserting = packet.readByte(ByteModification.NEG);
		final int fromSlot = packet.readShort(ByteOrder.LE, ByteModification.ADD);
		final int toSlot = packet.readShort(ByteOrder.LE);
		
		if (player.idle) {
			player.idle = false;
		}

		switch (interfaceId) {
		case InterfaceConstants.INVENTORY_INTERFACE:
		case InterfaceConstants.INVENTORY_STORE:
			player.inventory.swap(fromSlot, toSlot);
			break;

		case InterfaceConstants.WITHDRAW_BANK:
			player.bank.moveItem(inserting, fromSlot, toSlot);
			break;

		default:
			System.out.println("Unkown Item movement itemcontainer id: " + interfaceId);
			break;
		}
	}
}
