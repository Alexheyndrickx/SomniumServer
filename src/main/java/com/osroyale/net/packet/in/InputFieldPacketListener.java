package com.osroyale.net.packet.in;

import com.osroyale.content.DropDisplay;
import com.osroyale.content.DropDisplay.DropType;
import com.osroyale.content.ProfileViewer;
import com.osroyale.content.famehall.FameHandler;
import com.osroyale.content.simulator.DropSimulator;
import com.osroyale.content.store.impl.PersonalStore;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.MessageColor;

@PacketListenerMeta(142)
public class InputFieldPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final int component = packet.readInt();
        final String context = packet.getRS2String();

        if (component < 0) {
            return;
        }
        if (PlayerRight.isDeveloper(player) && player.debug) {
            player.send(new SendMessage("[InputField] - Text: " + context + " Component: " + component, MessageColor.DEVELOPER));
        }

        switch (component) {

        /* Clan chat */
            case 42102:
                player.forClan(clan -> {
                    if (clan.canManage(clan.getMember(player.getName()).orElse(null))) {
                        clan.setName(player, context);
                    }
                });
                break;
            case 42104:
                player.forClan(clan -> {
                    if (clan.canManage(clan.getMember(player.getName()).orElse(null))) {
                        clan.setTag(player, context);
                    }
                });
                break;
            case 42106:
                player.forClan(clan -> {
                    if (clan.canManage(clan.getMember(player.getName()).orElse(null))) {
                        clan.setSlogan(player, context);
                    }
                });
            case 42108: {
                player.forClan(clan -> {
                    if (clan.canManage(clan.getMember(player.getName()).orElse(null))) {
                        clan.getManagement().password = context;
                        if (context.isEmpty()) {
                            player.message("Your clan will no longer use a password." );
                        } else {
                            player.message("The new clan password is: " + context + "." );
                        }
                    }
                });
                break;
            }

        /* Personal Store */
            case 38307:
                PersonalStore.changeName(player, context, false);
                break;
            case 38309:
                PersonalStore.changeName(player, context, true);
                break;

        /* Drop simulator */
            case 26810:
                DropSimulator.drawList(player, context);
                break;

		/* Price checker */
            case 48508:
                player.priceChecker.searchItem(context);
                break;

		/* Hall of fame */
            case 58506:
                FameHandler.search(player, context);
                break;

		/* Preset */
            case 57021:
                player.presetManager.name(context);
                break;

		/* Drop display */
            case 54506:
                DropDisplay.search(player, context, DropType.ITEM);
                break;
            case 54507:
                DropDisplay.search(player, context, DropType.NPC);
                break;

		/* Friend's Profile view */
            case 353:
                if (World.search(context).isPresent()) {
                    ProfileViewer.open(player, World.search(context).get());
                    return;
                }

                player.send(new SendMessage("You can not view " + context + "'s profile as they are currently offline."));
                break;

        /* Friend's manage */
            case 354:
                if (PlayerRight.isModerator(player)) {
                    if (World.search(context).isPresent()) {
//                        StaffPanel.search(player, context);
                        return;
                    }

                    player.send(new SendMessage("You can not manage " + context + " as they are currently offline."));
                }
                break;
        }
    }
}
