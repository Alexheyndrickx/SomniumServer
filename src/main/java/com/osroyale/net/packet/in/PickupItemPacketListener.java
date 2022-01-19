package com.osroyale.net.packet.in;

import com.osroyale.game.world.entity.mob.data.PacketType;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.codec.ByteOrder;
import com.osroyale.net.packet.ClientPackets;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.content.event.EventDispatcher;
import com.osroyale.content.event.impl.PickupItemInteractionEvent;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Position;
import com.osroyale.util.MessageColor;

/**
 * The {@link GamePacket} responsible for picking up an item on the ground.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta(ClientPackets.PICKUP_GROUND_ITEM)
public class PickupItemPacketListener implements PacketListener {

    @Override
    public void handlePacket(final Player player, GamePacket packet) {
        if (player.locking.locked(PacketType.PICKUP_ITEM)) {
            return;
        }

        final int y = packet.readShort(ByteOrder.LE);
        final int id = packet.readShort(false);
        final int x = packet.readShort(ByteOrder.LE);

        final Item item = new Item(id);
        final Position position = Position.create(x, y, player.getHeight());

        if (EventDispatcher.execute(player, new PickupItemInteractionEvent(item, position))) {
            if (PlayerRight.isDeveloper(player) && player.debug) {
                player.send(new SendMessage(String.format("[%s]: item=%d position=%s", PickupItemInteractionEvent.class.getSimpleName(), item.getId(), position.toString())));
            }
            return;
        }

        player.pickup(item, position);
    }
}
