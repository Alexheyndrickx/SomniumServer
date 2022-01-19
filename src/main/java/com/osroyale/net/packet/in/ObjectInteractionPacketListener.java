package com.osroyale.net.packet.in;

import com.osroyale.content.event.EventDispatcher;
import com.osroyale.content.event.impl.FirstObjectClick;
import com.osroyale.content.event.impl.ObjectInteractionEvent;
import com.osroyale.content.event.impl.SecondObjectClick;
import com.osroyale.content.event.impl.ThirdObjectClick;
import com.osroyale.game.event.impl.ObjectClickEvent;
import com.osroyale.game.plugin.PluginManager;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.data.PacketType;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.game.world.object.GameObjectDefinition;
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
 * The {@code GamePacket} responsible for clicking various options of an in-game
 * object.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta({ClientPackets.FIRST_CLICK_OBJECT, ClientPackets.SECOND_CLICK_OBJECT, ClientPackets.THIRD_CLICK_OBJECT})
public class ObjectInteractionPacketListener implements PacketListener {

    @Override
    public void handlePacket(final Player player, GamePacket packet) {
        checkState(player != null, "[@ObjectInteraction] Player is null");
        if (player.locking.locked(PacketType.CLICK_OBJECT))
            return;

        switch (packet.getOpcode()) {
            case ClientPackets.FIRST_CLICK_OBJECT:
                handleFirstClickObject(player, packet);
                break;
            case ClientPackets.SECOND_CLICK_OBJECT:
                handleSecondClickObject(player, packet);
                break;
            case ClientPackets.THIRD_CLICK_OBJECT:
                handleThirdClickObject(player, packet);
                break;
        }
    }

    private static final int[] CHEAP_CODE = {5959, 1814, 5959, 5960, 9706};

    private static void handleFirstClickObject(Player player, GamePacket packet) {
        int x = packet.readShort(ByteOrder.LE, ByteModification.ADD);
        int id = packet.readShort(false);
        int y = packet.readShort(false, ByteModification.ADD);
        GameObjectDefinition objectDefinition = GameObjectDefinition.forId(id);

        if (objectDefinition == null)
            return;

        final Position position = Position.create(x, y, player.getHeight());
        Region region = World.getRegions().getRegion(x, y);
        GameObject object = region.getGameObject(id, position);

        if (object == null) {
            return;
        }

        for (int cheap : CHEAP_CODE) {
            if (object.getId() == cheap) {
                player.walkExactlyTo(object.getPosition(), () -> onAction(player, object, 1));
                return;
            }
        }

        player.walkTo(object, () -> onAction(player, object, 1));
    }

    private static void handleSecondClickObject(Player player, GamePacket packet) {
        final int id = packet.readShort(ByteOrder.LE, ByteModification.ADD);
        final int y = packet.readShort(ByteOrder.LE);
        final int x = packet.readShort(false, ByteModification.ADD);

        final GameObjectDefinition objectDefinition = GameObjectDefinition.forId(id);

        if (objectDefinition == null) {
            return;
        }

        final Position position = Position.create(x, y, player.getHeight());
        final Region region = World.getRegions().getRegion(position);

        final GameObject object = region.getGameObject(id, position);

        if (object == null) {
            return;
        }

        player.walkTo(object, () -> onAction(player, object, 2));
    }

    private static void handleThirdClickObject(Player player, GamePacket packet) {
        final int x = packet.readShort(ByteOrder.LE);
        final int y = packet.readShort(false);
        final int id = packet.readShort(false, ByteOrder.LE, ByteModification.ADD);

        final GameObjectDefinition objectDefinition = GameObjectDefinition.forId(id);

        if (objectDefinition == null) {
            return;
        }

        final Position position = Position.create(x, y, player.getHeight());
        final Region region = World.getRegions().getRegion(position);
        final GameObject object = region.getGameObject(id, position);

        if (object == null) {
            return;
        }

        player.walkTo(object, () -> onAction(player, object, 3));
    }

    private static void onAction(Player player, GameObject object, int type) {
        player.face(object);
        ObjectInteractionEvent event;

        if (type == 2) {
            event = new SecondObjectClick(object);
        } else if (type == 3) {
            event = new ThirdObjectClick(object);
        } else {
            event = new FirstObjectClick(object);
        }

        if (EventDispatcher.execute(player, event)) {
            return;
        }

        PluginManager.getDataBus().publish(player, new ObjectClickEvent(type, object));
    }
}
