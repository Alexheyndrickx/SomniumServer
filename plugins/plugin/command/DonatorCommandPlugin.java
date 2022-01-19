package plugin.command;

import com.osroyale.Config;
import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.content.skill.impl.magic.teleport.TeleportationData;
import com.osroyale.game.plugin.extension.CommandExtension;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.command.Command;
import com.osroyale.game.world.entity.mob.player.command.CommandParser;
import com.osroyale.net.packet.out.SendMessage;


public class DonatorCommandPlugin extends CommandExtension {

    @Override
    protected void register() {
        commands.add(new Command("donatorzone", "donorzone", "dzone", "dz") {
            @Override
            public void execute(Player player, CommandParser parser) {
                Teleportation.teleport(player, Config.DONATOR_ZONE, 20, TeleportationData.DONATOR, () -> {
                    player.send(new SendMessage("Welcome to the donator zone, " + player.getName() + "!"));
                });
            }
        });
    }

    @Override
    public boolean canAccess(Player player) {
        return PlayerRight.isDonator(player);
    }

}
