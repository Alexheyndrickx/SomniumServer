package com.osroyale.net.packet.in;

import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.items.containers.inventory.Inventory;
import com.osroyale.game.world.items.containers.pricechecker.PriceType;
import com.osroyale.util.MessageColor;
import com.osroyale.util.Utility;

/**
 * Handles the in examine packet.
 *
 * @author Daniel
 */
@PacketListenerMeta({150})
public class ExaminePacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        int slot = packet.readShort();
        int interfaceId = packet.readShort();
        int itemId = packet.readShort();

        if (PlayerRight.isDeveloper(player) && player.debug) {
            player.send(new SendMessage("[Examine] - slot: " + slot + " -- interfaceId: " + interfaceId + " -- itemId: " + itemId, MessageColor.DEVELOPER));
        }

        if (slot == -1) {
            player.settings.clientWidth = interfaceId;
            player.settings.clientHeight = itemId;
            return;
        }

        switch (interfaceId) {
            case Inventory.INVENTORY_DISPLAY_ID: {
                Item item = player.inventory.get(slot);
                if (item == null || item.getId() != itemId) return;
                if (item.isTradeable()) {
                    String message = "";
                    message += "Item: <col=A52929>" + item.getName() + "</col> ";
                    message += "Value: <col=A52929>" + Utility.formatDigits(item.getValue(PriceType.VALUE)) + " (" + Utility.formatDigits(item.getValue(PriceType.VALUE) * item.getAmount()) + ")</col> ";
                    message += "High alch: <col=A52929>" + Utility.formatDigits(item.getValue(PriceType.HIGH_ALCH_VALUE)) + "</col> ";
                    player.send(new SendMessage(message));
                }
                break;
            }
            case 26816: {
                if (!player.attributes.has("DROP_SIMULATOR_SORTED_LIST")) return;
                Item[] items = player.attributes.get("DROP_SIMULATOR_SORTED_LIST");
                if (slot < 0 || slot >= items.length) return;
                Item item = items[slot];
                if (item.isTradeable()) {
                    String message = "";
                    message += "Item: <col=A52929>" + item.getName() + "</col> ";
                    message += "Value: <col=A52929>" + Utility.formatDigits(item.getValue(PriceType.VALUE)) + " (" + Utility.formatDigits(item.getValue(PriceType.VALUE) * item.getAmount()) + ")</col> ";
                    message += "High alch: <col=A52929>" + Utility.formatDigits(item.getValue(PriceType.HIGH_ALCH_VALUE));
                    player.send(new SendMessage(message));
                }
                break;
            }

        }
    }

}
