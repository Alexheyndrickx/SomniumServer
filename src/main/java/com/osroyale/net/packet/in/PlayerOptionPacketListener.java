package com.osroyale.net.packet.in;

import com.osroyale.content.ProfileViewer;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.magic.CombatSpell;
import com.osroyale.game.world.entity.mob.data.PacketType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.exchange.duel.StakeSession;
import com.osroyale.game.world.entity.mob.player.exchange.trade.TradeSession;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.net.codec.ByteOrder;
import com.osroyale.net.packet.ClientPackets;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;

import static com.google.common.base.Preconditions.checkState;

/**
 * The {@link GamePacket}s responsible interacting with other players.
 * 
 * @author Daniel | Obey
 */
@PacketListenerMeta({ ClientPackets.TRADE_REQUEST, ClientPackets.TRADE_ANSWER, ClientPackets.CHALLENGE_PLAYER, ClientPackets.FOLLOW_PLAYER, ClientPackets.MAGIC_ON_PLAYER, ClientPackets.ATTACK_PLAYER })
public final class PlayerOptionPacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		checkState(player != null, "Player is null");
		
		/* Death block. */
		if (player.isDead()) {
			return;
		}

		/* If player is locked. */
		if (player.locking.locked(PacketType.INTERACT)) {
			return;
		}

		switch (packet.getOpcode()) {

		case 128:
			handleDuelRequest(player, packet);
			break;
			
		case 153:
			handleAttackPlayer(player, packet);
			break;

		case 73:
			handleFollowPlayer(player, packet);
			break;
			
		case 139:
			handleTradeRequest(player, packet);
			break;
			
		case 39:
			handleReportAbuse(player, packet);
			break;
			
		case ClientPackets.MAGIC_ON_PLAYER:
			handleMagicOnPlayer(player, packet);
			break;
		}
	}
	
	private static boolean isValid(Player other) {
		Position position = other.getPosition();
		Region region = World.getRegions().getRegion(position);
		return region.containsPlayer(other.getHeight(), other) && other.isValid();
	}

	private void handleDuelRequest(Player player, GamePacket packet) {
		final int index = packet.readShort();
		
		World.getPlayerBySlot(index).ifPresent(other -> {

			if (!isValid(other)) {
				return;
			}

			player.walkTo(other, () -> player.exchangeSession.request(new StakeSession(player, other)));
		});
	}

	private void handleAttackPlayer(Player player, GamePacket packet) {
		int index = packet.readShort(ByteOrder.LE);
		World.getPlayerBySlot(index).ifPresent(other -> {

			if (!isValid(other)) {
				return;
			}

			player.getCombat().attack(other);
		});
	}
	
	private void handleFollowPlayer(Player player, GamePacket packet) {
		final int index = packet.readShort(ByteOrder.LE);
		
		World.getPlayerBySlot(index).ifPresent(other -> {
			if (!isValid(other)) return;
			player.follow(other);
		});
	}

	private void handleTradeRequest(Player player, GamePacket packet) {
		final int index = packet.readShort(ByteOrder.LE);
		World.getPlayerBySlot(index).ifPresent(other -> {
			if (!isValid(other)) return;
			player.walkTo(other, () -> player.exchangeSession.request(new TradeSession(player, other)));
		});
	}
	
	private void handleReportAbuse(Player player, GamePacket packet) {
		final int index = packet.readShort(ByteOrder.LE);

		World.getPlayerBySlot(index).ifPresent(other -> {
			if (!isValid(other)) return;
			ProfileViewer.open(player, other);
//			player.send(new SendMessage("Report abuse request sent to: " + other.getName() + " (" + index + ")"));
		});
	}
	
	private void handleMagicOnPlayer(Player player, GamePacket packet) {
		int index = packet.readShort(ByteModification.ADD);
		int spell = packet.readShort(ByteOrder.LE);
		CombatSpell combatSpell = CombatSpell.get(spell);

		World.getPlayerBySlot(index).ifPresent(other -> {
			if (!other.isValid()) {
				return;
			}

			if (combatSpell == null) {
				return;
			}

			if (player.spellbook != combatSpell.getSpellbook()) {
				return;
			}

			player.setSingleCast(combatSpell);

			if (!player.getCombat().attack(other)) {
				player.setSingleCast(null);
				player.resetFace();
			}
		});
	}
}
