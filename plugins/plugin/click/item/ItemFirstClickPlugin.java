package plugin.click.item;

import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.achievement.AchievementWriter;
import com.osroyale.content.skill.impl.slayer.Slayer;
import com.osroyale.content.skill.impl.slayer.SlayerTask;
import com.osroyale.content.skill.impl.woodcutting.BirdsNest;
import com.osroyale.content.writer.InterfaceWriter;
import com.osroyale.game.action.impl.SpadeAction;
import com.osroyale.game.event.impl.ItemClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.Utility;

public class ItemFirstClickPlugin extends PluginContext {

    @Override
    protected boolean firstClickItem(Player player, ItemClickEvent event) {
        switch (event.getItem().getId()) {

            case 12791:
                player.runePouch.open();
                break;

            case 4155: {
                Slayer slayer = player.slayer;
                SlayerTask task = slayer.getTask();
                player.send(new SendMessage(task == null ? "You currently don't have a task, visit Nieve in edgeville to be assigned one." : String.format("You're assigned to kill %s; only %d more to go.", task.getName(), slayer.getAmount())));
            }
            break;

            case 405: {
                int coins = Utility.random(50000, 75000);
                player.inventory.remove(405, 1);
                player.inventory.add(995, coins);
                player.message("You found " + Utility.formatDigits(coins) + " coins inside of the casket!");
                AchievementHandler.activate(player, AchievementKey.CASKET);
                break;
            }

            case 21034:
                if (player.unlockedPrayers.contains(Prayer.RIGOUR)) {
                    player.dialogueFactory.sendStatement("You already have this prayer unlocked!").execute();
                    return true;
                }

                player.inventory.remove(event.getItem());
                player.unlockedPrayers.add(Prayer.RIGOUR);
                player.dialogueFactory.sendStatement("You have learned the Rigour prayer!").execute();
                break;

            case 21079:
                if (player.unlockedPrayers.contains(Prayer.AUGURY)) {
                    player.dialogueFactory.sendStatement("You already have this prayer unlocked!").execute();
                    return true;
                }

                player.inventory.remove(event.getItem());
                player.unlockedPrayers.add(Prayer.AUGURY);
                player.dialogueFactory.sendStatement("You have learned the Augury prayer!").execute();
                break;


            case 2528:
                player.send(new SendString("Genie's Experience Lamp", 2810));
                player.send(new SendString("", 2831));
                player.interfaceManager.open(2808);
                break;

            /* Spade */
            case 952:
                player.action.execute(new SpadeAction(player), true);
                break;

            /* Looting bag. */
            case 11941:
                player.lootingBag.open();
                break;

			/* Birds nest. */
            case 5070:
            case 5071:
            case 5072:
            case 5073:
            case 5074:
                BirdsNest.search(player, event.getItem().getId());
                break;

            default:
                return false;

        }
        return true;
    }

}
