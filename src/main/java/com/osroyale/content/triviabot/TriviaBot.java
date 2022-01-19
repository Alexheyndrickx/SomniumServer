package com.osroyale.content.triviabot;

import com.osroyale.Config;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

import java.util.*;

import static com.osroyale.game.world.items.containers.bank.VaultCurrency.BLOOD_MONEY;

/**
 * Manages the trivia bot system.
 *
 * @author Daniel
 */
public class TriviaBot {

    /** Holds all the bot data. */
    private final static Set<TriviaBotData> DATA = new HashSet<>();

    /** The current question/answer set. */
    private static TriviaBotData CURRENT = null;

    /** Color of the TriviaBot messages. */
    private static final String COLOR = "<col=354CE6>";

    /** Declares the TriviaBot data. */
    public static void declare() {
        Collections.addAll(DATA, TriviaBotData.values());
    }

    /** Assigns a new question */
    public static void assign() {
        CURRENT = Utility.randomElement(DATA);
        World.sendMessage(COLOR + "<icon=21> TriviaBot: </col>" + CURRENT.getQuestion(), player -> player.settings.triviaBot);
    }

    /** Handles player answering the question */
    public static void answer(Player player, String answer) {
        if (!player.settings.triviaBot) {
            return;
        }
        if (CURRENT == null) {
            player.send(new SendMessage(COLOR + "<icon=21> TriviaBot: </col>There is no question currently assigned!"));
            return;
        }
        if (Arrays.stream(Config.BAD_STRINGS).anyMatch(answer::contains)) {
            player.send(new SendMessage(COLOR + "<icon=21> TriviaBot: </col>You think you're funny, don't you? Guess what? You ain't."));
            return;
        }
        if (Arrays.stream(CURRENT.getAnswers()).anyMatch(a -> a.equalsIgnoreCase(answer))) {
            answered(player, answer);
            return;
        }
        if (Utility.random(3) == 0) {
            player.speak("Golly gee! I just entered a wrong trivia answer!");
        }
        player.send(new SendMessage(COLOR + "<icon=21> TriviaBot: </col>Sorry, the answer you have entered is incorrect! Try again!"));
    }

    /** Handles player answering the question successfully */
    private static void answered(Player player, String answer) {
        String color = player.right.getColor();
        int reward = Utility.random(50, 150);

        player.bankVault.add(BLOOD_MONEY, reward);
        AchievementHandler.activate(player, AchievementKey.TRIVIABOT, 1);
        player.send(new SendMessage(Utility.formatDigits(reward) + " blood money were added into your bank vault."));
        World.sendMessage(COLOR + "<icon=21> TriviaBot: <col=" + color + ">" + player.getName() + "</col> has answered the question correctly! Answer: " + COLOR + Utility.capitalizeSentence(answer) + "</col>.");
        CURRENT = null;
    }
}
