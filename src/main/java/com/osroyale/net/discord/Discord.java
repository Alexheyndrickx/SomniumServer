package com.osroyale.net.discord;


import com.osroyale.Config;
import com.osroyale.game.world.WorldType;
import com.osroyale.util.Stopwatch;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;

/**
 * Handles creating a connection to the OS Royale community discord channel.
 *
 * !!! - > Better way of handling the community and administration message instance.
 * When creating the channel instance by using client.getChannelById(long)
 * the channel is always null. I am "cheap haxing" it by initializing the channel
 * in the onMessage event. Problem with this is if you start the server from fresh the
 * Discord bot will not be able to send outgoing messages (message) until someone
 * Has triggered the discord event.
 *
 * @author Daniel
 */
public class Discord {
    /** The community channel identification. */
    static final long COMMUNITY_CHANNEL = 000;

    /** The client builder. */
    public static ClientBuilder builder;

    /** The discord client. */
    public static IDiscordClient client;

    /** The community channel instance. */
    static IChannel community;

    /** The discord stopwatch (prevent spamming). */
    public static Stopwatch stopwatch = Stopwatch.start();

    /** Handles starting the discord bot. */
    public static void start() {
        if (Config.WORLD_TYPE == WorldType.LIVE) {
            builder = new ClientBuilder().withToken(Config.DISCORD_TOKEN);
            client = builder.login();
            client.getDispatcher().registerListener(new DiscordDispatcher());
        }
    }

    public static void message(String message) {
        if (Config.WORLD_TYPE == WorldType.LIVE && community != null) {
            community.sendMessage(message);
        }
    }
}
