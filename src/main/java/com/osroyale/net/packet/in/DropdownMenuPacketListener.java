package com.osroyale.net.packet.in;

import com.osroyale.content.clanchannel.ClanRank;
import com.osroyale.content.clanchannel.ClanRepository;
import com.osroyale.content.clanchannel.ClanType;
import com.osroyale.content.clanchannel.content.ClanViewer;
import com.osroyale.content.simulator.DropSimulator;
import com.osroyale.content.simulator.Simulation;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.MessageColor;

/**
 * The {@code GamePacket} responsible for dropdown menus.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta(255)
public class DropdownMenuPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final int identification = packet.readInt();
        final int value = packet.readByte();

        if (identification < 0)
            return;
        if (value < 0)
            return;

        if (player.debug && PlayerRight.isDeveloper(player)) {
            player.send(new SendMessage("[DropdownMenuPacketListener] Identification: " + identification + " | Value: " + value, MessageColor.DEVELOPER));
        }

        switch (identification) {
            /* Clan Viewer */
            case 43019:
                player.clanViewer.filter = ClanViewer.Filter.values()[value];
                player.clanViewer.open(player.clanChannel, ClanViewer.ClanTab.OVERVIEW);
                break;
            case 42110:
                player.forClan(channel -> {
                    if (channel.canManage(channel.getMember(player.getName()).orElse(null))) {
                        ClanRepository.getTopChanels(channel.getDetails().type).ifPresent(set -> set.remove(channel));
                        channel.getDetails().type = ClanType.values()[value];
                        ClanRepository.getTopChanels(ClanType.values()[value]).ifPresent(set -> set.add(channel));
                        ClanRepository.ALLTIME.add(channel);
                        player.clanViewer.update(channel);
                    }
                });
                break;
            case 42112:
                player.forClan(channel -> {
                    if (channel.canManage(channel.getMember(player.getName()).orElse(null))) {
                        channel.getManagement().setEnterRank(ClanRank.values()[value]);
                        player.clanViewer.update(channel);
                    }
                });
                break;
            case 42114:
                player.forClan(channel -> {
                    if (channel.canManage(channel.getMember(player.getName()).orElse(null))) {
                        channel.getManagement().setTalkRank(ClanRank.values()[value]);
                        player.clanViewer.update(channel);
                    }
                });
                break;
            case 42116:
                player.forClan(channel -> {
                    if (channel.canManage(channel.getMember(player.getName()).orElse(null))) {
                        channel.getManagement().setManageRank(ClanRank.values()[value]);
                        player.clanViewer.update(channel);
                    }
                });
                break;
            case 42134:
                String color = null;

                switch (value) {
                    case 0:
                        color = "<col=ffffff>";
                        break;
                    case 1:
                        color = "<col=F03737>";
                        break;
                    case 2:
                        color = "<col=2ADE36>";
                        break;
                    case 3:
                        color = "<col=2974FF>";
                        break;
                    case 4:
                        color = "<col=EBA226>";
                        break;
                    case 5:
                        color = "<col=A82D81>";
                        break;
                    case 6:
                        color = "<col=FF57CA>";
                        break;
                }

                final String col = color;
                player.forClan(channel -> {
                    if (channel.canManage(channel.getMember(player.getName()).orElse(null))) {
                        channel.setColor(col);
                        player.clanViewer.update(channel);
                    }
                });
                break;

            /* Drop Simulator. */
            case 26811:
                final int[] simulations = {10, 100, 1000, 10000, 100000};
                int simulatorNpc = player.attributes.get("DROP_SIMULATOR_KEY");
                DropSimulator.simulate(player, Simulation.NPC_DROP, simulatorNpc, simulations[value]);
                break;
        }
    }
}
