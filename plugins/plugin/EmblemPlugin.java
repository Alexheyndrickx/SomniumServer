package plugin;

import com.osroyale.content.EmblemData;
import com.osroyale.content.combat.SkullHeadIconType;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.store.Store;
import com.osroyale.game.event.impl.NpcClickEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.items.Item;
import com.osroyale.util.Utility;

import java.util.HashSet;
import java.util.Set;

public class EmblemPlugin extends PluginContext {

    private void skull(DialogueFactory factory) {
        factory.sendOption("White (protect item permitted)", () -> {
            factory.getPlayer().skulling.skull(SkullHeadIconType.WHITE_SKULL);
        }, "Red (no protect item permitted, all items lost on death)", () -> {
            factory.getPlayer().prayer.deactivate(Prayer.PROTECT_ITEM);
            factory.getPlayer().skulling.skull(SkullHeadIconType.RED_SKULL);
        }, "Nevermind", factory::clear);
    }

    private void exchangeEmblems(DialogueFactory factory) {
        Player player = factory.getPlayer();
        Set<Item> items = new HashSet<>();
        int value = 0;

        for (Item item : player.inventory) {
            if (item == null)
                continue;
            EmblemData emblem = EmblemData.forId(item.getId());
            if (emblem == null)
                continue;
            items.add(item);
            value += emblem.bloodMoney;
        }

        if (items.isEmpty()) {
            factory.sendNpcChat(315, "You do not have any emblems to sell!");
            return;
        }

        int finalValue = value;
        factory.sendNpcChat(315, "I will purchase all your emblems for", Utility.formatDigits(value) + " blood money. Would you like", "to continue?");
        factory.sendOption("Yes!", () -> {
            if (player.inventory.containsAll(items)) {
                player.inventory.removeAll(items);
                player.inventory.add(13307, finalValue);
                factory.sendNpcChat(315, "Thanks for the business #name!");
            }
        }, "No thanks!", factory::clear);
    }

    private void exchangeCoins(DialogueFactory factory, int coins) {
        Player player = factory.getPlayer();
        int rate = coins / 1_000;

        if (!player.inventory.contains(995, coins)) {
            factory.sendNpcChat(315, "You do not have " + Utility.formatDigits(coins) + " coins to exchange!");
            return;
        }

        if (player.inventory.isFull()) {
            factory.sendNpcChat(315, "You do not have enough free inventory spaces to do this!");
            return;
        }

        player.inventory.remove(995, coins);
        player.inventory.add(13307, rate);
        factory.sendNpcChat(315, "Blood money exchange was successful!");
    }

    private void emblems(DialogueFactory factory) {
        factory.sendOption("How do I get emblems?", () -> {
            factory.sendNpcChat(315, "Emblems can be earned by killing wilderness bosses.",
                    "An ancient emblem is the highest form of emblem and",
                    "can only be earned from the revenant cave.",
                    "Players may upgrade their emblem level by");
            factory.sendNpcChat(315, "Killing other players with an emblem in their inventory.",
                    "All emblems can be sold to me for Blood money.", "Happy hunting!");
        }, "Sell mysterious emblems", () -> {
            exchangeEmblems(factory);
        });
    }

    private void resetKDR(DialogueFactory factory) {
        Player player = factory.getPlayer();
        factory.sendNpcChat(316, "That will cost you 10,000 blood money.", "Would you like to proceed?");
        factory.sendOption("Reset my KDR", () -> {
            if (!player.inventory.contains(13307, 10000)) {
                factory.sendNpcChat(316, "You don't have enough blood money!");
                return;
            }
            player.kill = 0;
            player.death = 0;
            player.inventory.remove(13307, 10000);
            factory.sendNpcChat(316, "Your KDR has been reset.");
        }, "No, don't do it", factory::clear);
    }

    @Override
    protected boolean firstClickNpc(Player player, NpcClickEvent event) {
        if (event.getNpc().id == 315) {
            DialogueFactory factory = player.dialogueFactory;
            factory.sendNpcChat(315, "Greetings adventurer!", "What are you looking for today?");
            factory.sendOption("Talk about emblems", () -> {
                emblems(factory);
            }, "Reset my KDR", () -> {
                resetKDR(factory);
            }, "I would like a wilderness skull", () -> {
                skull(factory);
            }, "Exchange coins for blood money", () -> {
                factory.sendNpcChat(315, "I am able to convert coins into blood money.", "Rate: 1,000,000 coins = 1,000 BM", "This action can not be undone!", "Would you like to proceed?");
                factory.sendOption("Exchange <col=255>1,000,000</col> coins (<col=D13636>1,000</col> BM)", () -> {
                    exchangeCoins(factory, 1_000_000);
                }, "Exchange <col=255>5,000,000</col> coins (<col=D13636>5,000</col> BM)", () -> {
                    exchangeCoins(factory, 5_000_000);
                }, "Exchange <col=255>10,000,000</col> coins (<col=D13636>10,000</col> BM)", () -> {
                    exchangeCoins(factory, 10_000_000);
                }, "Exchange <col=255>50,000,000</col> coins (<col=D13636>50,000</col> BM)", () -> {
                    exchangeCoins(factory, 50_000_000);
                }, "Exchange <col=255>100,000,000</col> coins (<col=D13636>100,000</col> BM)", () -> {
                    exchangeCoins(factory, 100_000_000);
                });
            }, "View stores", () -> {
                factory.sendOption("Melee Store", () -> {
                    Store.STORES.get("Blood Melee Store").open(player);
                }, "Barrows Store", () -> {
                    Store.STORES.get("Blood Barrows Store").open(player);
                }, "Range Store", () -> {
                    Store.STORES.get("Blood Range Store").open(player);
                }, "Magic Store", () -> {
                    Store.STORES.get("Blood Magic Store").open(player);
                }, "Miscellaneous Store", () -> {
                    Store.STORES.get("Blood Miscellaneous Store").open(player);
                });
            });
            factory.execute();
            return true;
        }
        return false;
    }

    @Override
    protected boolean secondClickNpc(Player player, NpcClickEvent event) {
        if (event.getNpc().id == 315) {
            player.dialogueFactory.sendOption("Melee Store", () -> {
                Store.STORES.get("Blood Melee Store").open(player);
            }, "Barrows Store", () -> {
                Store.STORES.get("Blood Barrows Store").open(player);
            }, "Range Store", () -> {
                Store.STORES.get("Blood Range Store").open(player);
            }, "Magic Store", () -> {
                Store.STORES.get("Blood Magic Store").open(player);
            }, "Miscellaneous Store", () -> {
                Store.STORES.get("Blood Miscellaneous Store").open(player);

            }).execute();
            return true;
        }
        return false;
    }

    @Override
    protected boolean thirdClickNpc(Player player, NpcClickEvent event) {
        if (event.getNpc().id == 315) {
            DialogueFactory factory = player.dialogueFactory;
            skull(factory);
            factory.execute();
            return true;
        }
        return false;
    }
}
