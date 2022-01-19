package com.osroyale.net.packet.in;

import com.osroyale.game.event.impl.CommandEvent;
import com.osroyale.game.plugin.PluginManager;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.command.CommandParser;
import com.osroyale.game.world.entity.mob.player.relations.ChatMessage;
import com.osroyale.net.packet.ClientPackets;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.util.Utility;

import java.util.Arrays;

/**
 * The {@code GamePacket} responsible for handling user commands send from the
 * client.
 *
 * @author Michael | Chex
 */
@PacketListenerMeta(ClientPackets.PLAYER_COMMAND)
public final class CommandPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final String input = packet.getRS2String().trim().toLowerCase();

        if (input.isEmpty() || input.length() > ChatMessage.CHARACTER_LIMIT) {
            return;
        }

        final CommandParser parser = CommandParser.split(input, " ");

        if (parser.getCommand().startsWith("/")) {
            if (player.punishment.isMuted()) {
                player.message("You can not send clan messages while muted!");
                return;
            }

            player.forClan(channel -> {
                CommandParser copy = CommandParser.split(input, "/");
                if (copy.hasNext()) {
                    final String line = copy.nextLine();
                    channel.chat(player.getName(), Utility.capitalizeSentence(line));
                }
            });
            return;
        }

        PluginManager.getDataBus().publish(player, new CommandEvent(parser));
    }

}
