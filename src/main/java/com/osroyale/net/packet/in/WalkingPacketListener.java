
package com.osroyale.net.packet.in;

import com.osroyale.content.activity.Activity;
import com.osroyale.content.activity.impl.duelarena.DuelArenaActivity;
import com.osroyale.content.activity.impl.duelarena.DuelRule;
import com.osroyale.game.Animation;
import com.osroyale.game.world.entity.mob.data.LockType;
import com.osroyale.game.world.entity.mob.data.PacketType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.codec.ByteModification;
import com.osroyale.net.codec.ByteOrder;
import com.osroyale.net.packet.ClientPackets;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.out.SendMessage;

import java.util.Optional;

/**
 * A packet which handles walking requests.
 *
 * @author Graham Edgecombe
 */
@PacketListenerMeta({ClientPackets.WALK_ON_COMMAND, ClientPackets.REGULAR_WALK, ClientPackets.MAP_WALK})
public class WalkingPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        if (player.locking.locked(PacketType.WALKING)) {
            if (player.locking.locked(LockType.STUN)) {
                player.send(new SendMessage("You are currently stunned."));
                player.getCombat().reset();
            }
            if (player.locking.locked(LockType.FREEZE)) {
                player.send(new SendMessage("A magical force stops you from moving!"));
                player.getCombat().reset();
            }
            return;
        }

        if (Activity.search(player, DuelArenaActivity.class).isPresent()) {
            DuelArenaActivity activity = Activity.search(player, DuelArenaActivity.class).get();
            if (activity.rules.contains(DuelRule.NO_MOVEMENT)) {
                player.send(new SendMessage("You cannot move in the duel arena."));
                player.getCombat().reset();
                return;
            }
        }

        player.skills.resetSkilling();

        if (player.resting) {
            player.animate(new Animation(-1));
            player.resting = false;
        }

		/* Dialogues */
        if (player.dialogue.isPresent()) {
            player.dialogue = Optional.empty();
        }

		/* Idle */
        if (player.idle) {
            player.idle = false;
        }

		/* Dialogue factory */
        if (!player.dialogueFactory.getChain().isEmpty()) {
            player.dialogueFactory.clear();
        }

		/* Dialogue options */
        if (player.optionDialogue.isPresent()) {
            player.optionDialogue = Optional.empty();
        }

        if (!player.interfaceManager.isMainClear()) {
            player.interfaceManager.close();
        }

        if (!player.interfaceManager.isDialogueClear()) {
            player.dialogueFactory.clear();
        }

		/* Reset the face. */
        player.resetFace();

		/* Clear non walkable actions */
        player.action.clearNonWalkableActions();
        player.resetWaypoint();
        player.getCombat().reset();

        final int targetX = packet.readShort(ByteOrder.LE);
        final int targetY = packet.readShort(ByteOrder.LE,  ByteModification.ADD);

        // the tile the player is trying to get to
        final Position destination = Position.create(targetX, targetY);

        // prevents the player from hacking the client to make the player walk really far distances.
        if (player.getPosition().getDistance(destination) > 32) {
            return;
        }

        boolean runQueue = packet.readByte(ByteModification.NEG) == 1;
        if (runQueue && PlayerRight.isDeveloper(player)) {
            player.move(destination);
        }
        player.movement.setRunningQueue(runQueue);
        player.movement.dijkstraPath(destination);
    }
}