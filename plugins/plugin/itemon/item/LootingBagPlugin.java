package plugin.itemon.item;

import com.osroyale.game.event.impl.ItemOnItemEvent;
import com.osroyale.game.event.impl.ItemOnObjectEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.items.Item;

public class LootingBagPlugin extends PluginContext {

    @Override
    protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
        if (event.getUsed().getId() != 11941) {
            return false;
        }

        if (!event.getObject().getDefinition().getName().equalsIgnoreCase("bank booth")) {
            return false;
        }

        if (player.right != PlayerRight.ULTIMATE_IRONMAN) {
            return false;
        }

        if (player.lootingBag.isEmpty()) {
            player.dialogueFactory.sendStatement("Your looting bag is empty!").execute();
            return true;
        }

        int inventory = player.inventory.getFreeSlots();
        int size = player.lootingBag.toNonNullArray().length;

        for (int index = 0; index < size; index++) {
            if (index >= inventory) {
                player.message("You do not have enough space to withdraw all your items!");
                break;
            }

            Item item = player.lootingBag.get(index);
            player.lootingBag.remove(item);
            player.inventory.add(item);
        }

        player.lootingBag.shift();
        return true;
    }

    @Override
    protected boolean itemOnItem(Player player, ItemOnItemEvent event) {
        final Item used = event.getUsed();
        final Item with = event.getWith();
        if (!(used.getId() == 11941 || with.getId() == 11941)) {
            return false;
        }

        if (used.getId() == 11941) {
            if (with.getAmount() == 1) {
                player.lootingBag.deposit(with, with.getAmount());
                return true;
            }
            player.lootingBag.depositMenu(with);
            return true;
        }

        if (with.getId() == 11941) {
            if (used.getAmount() == 1) {
                player.lootingBag.deposit(used, used.getAmount());
                return true;
            }
            player.lootingBag.depositMenu(used);
            return true;
        }
        return false;
    }

}
