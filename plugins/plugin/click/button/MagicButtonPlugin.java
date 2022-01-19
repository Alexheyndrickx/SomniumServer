package plugin.click.button;

import com.osroyale.Config;
import com.osroyale.content.skill.impl.magic.spell.impl.BonesToBananas;
import com.osroyale.content.skill.impl.magic.spell.impl.BonesToPeaches;
import com.osroyale.content.skill.impl.magic.spell.impl.Vengeance;
import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.content.skill.impl.magic.teleport.TeleportationData;
import com.osroyale.game.event.impl.ItemContainerContextMenuEvent;
import com.osroyale.game.plugin.PluginContext;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.position.Position;

public class MagicButtonPlugin extends PluginContext {

    @Override
    protected boolean onClick(Player player, int button) {
        switch (button) {
            case 1167:
                if (player.getCombat().isUnderAttackByPlayer() && !PlayerRight.isDeveloper(player)) {
                    player.message("You can't do this right now!");
                    return true;
                }
                player.dialogueFactory.sendOption("Lumbridge", () -> {
                    Teleportation.teleport(player, new Position(3222, 3218, 0));
                }, "Varrock", () -> {
                    Teleportation.teleport(player, new Position(3210, 3424, 0));
                }, "Falador", () -> {
                    Teleportation.teleport(player, new Position(2964, 3378, 0));
                }, "Yanille", () -> {
                    Teleportation.teleport(player, new Position(2606, 3093, 0));
                }, "Camelot", () -> {
                    Teleportation.teleport(player, new Position(2757, 3477, 0));
                }).execute();
                break;
            case 19210:
            case 21741:
            case 30000:
            case -25436:
                if (player.getCombat().isUnderAttackByPlayer() && !PlayerRight.isDeveloper(player)) {
                    player.message("You can not teleport whilst in combat!");
                    return true;
                }
                if (PlayerRight.isModerator(player)) {
                    player.dialogueFactory.sendOption("Home", () -> Teleportation.teleport(player, Config.DEFAULT_POSITION), "Donator Zone", () -> Teleportation.teleport(player, Config.DONATOR_ZONE, TeleportationData.DONATOR), "Staff Zone", () -> Teleportation.teleport(player, Config.STAFF_ZONE, TeleportationData.DONATOR)).execute();
                    return true;
                }
                if (PlayerRight.isDonator(player)) {
                    player.dialogueFactory.sendOption("Home", () -> Teleportation.teleport(player, Config.DEFAULT_POSITION), "Donator Zone", () -> Teleportation.teleport(player, Config.DONATOR_ZONE, TeleportationData.DONATOR)).execute();
                    return true;
                }
                Teleportation.teleport(player, Config.DEFAULT_POSITION);
                return true;
            case 1159:
                player.spellCasting.cast(new BonesToBananas(), null);
                return true;
            case 15877:
                player.spellCasting.cast(new BonesToPeaches(), null);
                return true;
            case 30306:
                player.spellCasting.cast(new Vengeance(), null);
                return true;
            case -25426:
                player.spellCasting.openBoltEnchant();
                return true;
        }
        return false;
    }

    @Override
    protected boolean firstClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
        if (event.getInterfaceId() == 42752) {
            player.spellCasting.enchant(event.getRemoveId());
            return true;
        }
        return false;
    }
}
