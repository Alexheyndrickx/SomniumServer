package com.osroyale.net.discord;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;

/**
 * The discord dispatcher.
 *
 * @author Daniel
 */
public class DiscordDispatcher {

    @EventSubscriber
    public void OnMesageEvent(MessageReceivedEvent event) throws DiscordException, MissingPermissionsException {
//        IMessage message = event.getMessage();
        IChannel channel = event.getChannel();
        long channelId = channel.getLongID();
//        String command = message.getContent().toLowerCase();

        if (Discord.community == null && channelId == Discord.COMMUNITY_CHANNEL) {
            Discord.community = channel;
        }
//
//        switch (command) {
//            case "::commands": {
//                StringBuilder builder = new StringBuilder();
//                builder.append(":uptime - displays the current server uptime | ");
//                builder.append("::players - displays the server's current player count | ");
//                builder.append("::staffonline - displays the server's current staff count | ");
//                channel.sendMessage(builder.toString());
//                break;
//            }
//            case "::uptime":
//                channel.sendMessage("OS Royale has been up for " + Utility.getUptime() + "!");
//                break;
//            case "::players":
//                channel.sendMessage("There are currently " + World.getPlayerCount() + " players online!");
//                break;
//            case "::staffonline": {
//                List<Player> staffs = World.getStaff();
//                if (staffs.isEmpty()) {
//                    channel.sendMessage("There are no staff members online! Don't get any ideas, I am always watching :)");
//                    return;
//                }
//
//                StringBuilder builder = new StringBuilder();
//
//                for (int index = 0; index < staffs.size(); index++) {
//                    Player staff = staffs.get(index);
//                    String string = staff.getName() + " (" + staff.right.getTitle() + ")" + (index == (staffs.size() - 1) ? "" : ", ");
//                    builder.append(string);
//                }
//
//                channel.sendMessage(builder.toString());
//                break;
//            }
        }
    }