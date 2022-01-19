package com.osroyale.content.prestige;

import com.osroyale.game.Animation;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendScrollbar;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.MessageColor;
import com.osroyale.util.Utility;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.osroyale.game.world.items.containers.bank.VaultCurrency.COINS;

/**
 * Handles the prestige class.
 *
 * @author Daniel.
 */
public class Prestige {

    /** The player instance. */
    private Player player;

    /** The total amount of prestiges. */
    public int totalPrestige;

    /** The prestige points. */
    private int prestigePoint;

    /** The prestiges. */
    public int[] prestige = new int[Skill.SKILL_COUNT];

    /** The set of all the active perks for the player. */
    public Set<PrestigePerk> activePerks = new HashSet<>();

    /** Animations that the player will do once prestiged. */
    private static final int[] ANIMATIONS = {7118, 2109, 862, 6303, 6999};

    /** Constructs a new <code>Prestige</code>. */
    public Prestige(Player player) {
        this.player = player;
    }

    /** Opens the prestige panel. */
    public void open() {
        player.send(new SendString(player.getName(), 52007));
        player.send(new SendString("Total Prestiges: <col=76E34B>" + totalPrestige + "</col>", 52008));
        player.send(new SendString("Prestige Points: <col=76E34B>" + prestigePoint + "</col>", 52009));
        Arrays.stream(PrestigeData.values()).forEach(p -> player.send(new SendString(p.name + " " + "(<col=" + getColorInterface(prestige[p.skill]) + ">" + prestige[p.skill] + "</col>)", p.string)));
        player.interfaceManager.open(52000);
    }

    /** Handles prestiging the skill. */
    public void prestige(PrestigeData data) {
        if (data.isCombatSkill() && !PlayerRight.isIronman(player)) {
            player.dialogueFactory.sendStatement("You can not prestige your combat skill!", "Only iron accounts are permitted to do so!").execute();
            return;
        }

        prestige[data.skill]++;
        totalPrestige += 1;
        prestigePoint += 1;
        player.skills.setExperience(data.skill, Skill.getExperienceForLevel(data.skill == 3 ? 10 : 1));
        player.skills.setMaxLevel(data.skill, data.skill == 3 ? 10 : 1);
        player.skills.setLevel(data.skill, data.skill == 3 ? 10 : 1);
        player.skills.refresh(data.skill);
        player.skills.setCombatLevel();
        player.updateFlags.add(UpdateFlag.APPEARANCE);
        player.animate(new Animation(Utility.randomElement(ANIMATIONS)));

        open();
        player.bankVault.add(COINS, 1_000_000);
        player.dialogueFactory.sendNpcChat(345, "I have successfully prestiged your <col=255>" + Skill.getName(data.skill) + "</col> skill.", "<col=255>1,000,000</col> coins were added to your vault.", "You now have <col=255>" + prestigePoint + "</col> prestige points and <col=255>" + totalPrestige + "</col> total", "prestiges.").execute();
    }

    /** Displays all the perk information. */
    public void perkInformation() {
        int size = PrestigePerk.values().length;
        for (int index = 0, string = 37114; index < size; index++) {
            PrestigePerk perk = PrestigePerk.forOrdinal(index).get();
            player.send(new SendString("<col=" + (hasPerk(perk) ? "347043" : "3c50b2") + ">" + perk.name, string++));
            player.send(new SendString(perk.description, string++));
            player.send(new SendString("", string++));
        }

        player.send(new SendString("<col=000000>This is a list of all the available perks.", 37111));
        player.send(new SendString("<col=000000>Perks with green names indicate that it is active.", 37112));
        player.send(new SendString("", 37113));
        player.send(new SendString("", 37107));
        player.send(new SendString("Prestige Perks Information", 37103));
        player.send(new SendScrollbar(37110, size * 65));
        player.send(new SendItemOnInterface(37199));
        player.interfaceManager.open(37100);
    }

    /** Activates the perk. */
    public boolean activatePerk(Item item) {
        if (!PrestigePerk.forItem(item.getId()).isPresent())
            return false;
        PrestigePerk perk = PrestigePerk.forItem(item.getId()).get();
        if (activePerks == null)
            activePerks = new HashSet<>();
        if (activePerks.contains(perk)) {
            player.send(new SendMessage("The Perk: " + perk.name + " perk is already active on your account!", MessageColor.DARK_BLUE));
            return true;
        }

        player.inventory.remove(item);
        activePerks.add(perk);
        player.send(new SendMessage("You have successfully activated the " + perk.name + " perk.", MessageColor.DARK_BLUE));
        return true;
    }

    public boolean hasPerk(PrestigePerk perk) {
        return activePerks.contains(perk);
    }

    /** Gets the current prestige color of the player. */
    public int getPrestigeColor(int skill) {
        return getColor(prestige[skill]);
    }

    /** Gets the prestige color based on the tier. */
    public int getColor(int tier) {
        switch (tier) {
            case 1:
                return 0xf49242;
            case 2:
                return 0x42f468;
            case 3:
                return 0x42d1f4;
            case 4:
                return 0xa76ffc;
            case 5:
                return 0xf44949;
            default:
                return 0xFFFF00;
        }
    }

    /** Gets the prestige color based on the tier for the itemcontainer. */
    private String getColorInterface(int tier) {
        return Integer.toHexString(getColor(tier));
    }

    public int getPrestigePoint() {
        return prestigePoint;
    }

    public void setPrestigePoint(int prestigePoint) {
        this.prestigePoint = prestigePoint;
    }
}
