package com.osroyale.content.dialogue.impl;

import com.osroyale.content.dialogue.Dialogue;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.dialogue.Expression;
import com.osroyale.content.store.Store;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.hit.HitIcon;
import com.osroyale.game.world.entity.combat.hit.Hitsplat;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.net.packet.out.SendURL;
import com.osroyale.util.Utility;

/**
 * Handles the vote dialogue.
 *
 * @author Daniel
 */
public class VoteDialogue extends Dialogue {

    @Override
    public void sendDialogues(DialogueFactory factory) {
        Player player = factory.getPlayer();
        factory.sendNpcChat(7481, Expression.FURIOUS, "HELLO " + factory.getPlayer().getName().toUpperCase() + "!!", "WHAT CAN I DO FOR YOUUU!?");

        factory.sendOption("WHY ARE YOU SCREAMING?", () -> {
            factory.sendPlayerChat("WHY ARE YOU SCREAMING?");
            factory.sendNpcChat(7481, "HAVE YOU SEEN WHAT I'M WEARING?", "WHO THE HELL PUT THIS CRAP ON ME!??", "WHY AM I THE VOTE NPC FOR OS ROYALE?", "I HAVE NO IDEA WHAT IS GOING ON AND I'M SCARED!");
        }, "Exchange vote token", () -> {
            World.schedule(1, () -> player.send(new SendInputAmount("How many vote tokens would you like to exchange?", 10, input -> exchange(factory, Integer.parseInt(input)))));
        }, "Show me your voting store!", () -> {
            Store.STORES.get("RunePK Vote Store").open(player);
        }, "I would like to vote to support this great server!", () -> {
            player.send(new SendURL("https://www.runepk.io/vote"));
            factory.sendNpcChat(7481, "THANK-YOU FOR VOTING!!!!");
        }, "Nevermind, I don't want to do anything for this server.", () -> {
            player.damage(new Hit(5, Hitsplat.CRITICAL, HitIcon.CANON));
            factory.clear();
        });
        factory.execute();
    }

    /** Handles redeeming vote tokens. */
    private void exchange(DialogueFactory factory, int amount) {
        Player player = factory.getPlayer();
        if (amount > 10) {
            factory.sendNpcChat(7481, Expression.SAD, "SORRY BUT YOU CAN ONLY EXCHANGE UP To", " 10 TOKENS AT A TIME!").execute();
            return;
        }

        int tokenAmount = player.inventory.computeAmountForId(7478);

        if (amount > tokenAmount)
            amount = tokenAmount;

        if (amount == 0) {
            factory.sendNpcChat(7481, Expression.SAD, "SORRY BUT YOU DO NOT HAVE ENOUGH", "VOTE TOKENS TO DO THIS!").execute();
            return;
        }

        if (player.inventory.getFreeSlots() < amount) {
            factory.sendNpcChat(7481, Expression.SAD, "YOU NEED AT LEAST " + amount + " FREE INVENTORY", "SPACES TO DO THIS!").execute();
            return;
        }

        player.votePoints += amount;
        player.inventory.remove(7478, amount);
        factory.sendNpcChat(7481, Expression.SAD, "I HAVE EXCHANGED " + amount + " VOTE TOKENS FOR YOU!", "YOU NOW HAVE " + Utility.formatDigits(player.votePoints) + " VOTE_KEY POINTS!").execute();
    }
}
