package com.osroyale.content.dialogue.impl;

import com.osroyale.content.dialogue.Dialogue;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.dialogue.Expression;
import com.osroyale.content.store.Store;
import com.osroyale.game.service.DonationService;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.out.SendURL;

/**
 * The royal king dialogue.
 *
 * @author Daniel
 */
public class RoyalKingDialogue extends Dialogue {

    private int index;

    public RoyalKingDialogue(int index) {
        this.index = index;
    }

    @Override
    public void sendDialogues(DialogueFactory factory) {
        if (index == 1) {
            claim(factory);
            factory.execute();
            return;
        }
        if (index == 2) {
            store(factory);
            factory.execute();
            return;
        }
        Player player = factory.getPlayer();
        factory.sendNpcChat(5523, Expression.HAPPY, "Hello adventurer, how may I help you?");
        factory.sendOption("Claim Purchase", () -> claim(factory), "Donator Information", () -> player.send(new SendURL("www.osroyale.com/store")), "My donation statistics", () -> myStats(factory), "Open Store", () -> store(factory), "Nevermind", factory::clear);
        factory.execute();
    }

    private void myStats(DialogueFactory factory) {
        factory.sendStatement("Money spent: $" + factory.getPlayer().donation.getSpent(), "Current credits: " + factory.getPlayer().donation.getCredits());
    }


    private void claim(DialogueFactory factory) {
        factory.onAction(() -> DonationService.claimDonation(factory.getPlayer()));
    }

    private void store(DialogueFactory factory) {
        factory.sendOption("Open Donator Store", () -> Store.STORES.get("Donator Store").open(factory.getPlayer()), "Ironman Donator Store", () -> Store.STORES.get("Ironman Donator Store").open(factory.getPlayer()),"Nevermind", factory::clear);
    }
}
