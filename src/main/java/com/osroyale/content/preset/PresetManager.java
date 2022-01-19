package com.osroyale.content.preset;

import com.osroyale.Config;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.game.world.entity.combat.magic.Autocast;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.prayer.Prayer;
import com.osroyale.game.world.entity.mob.prayer.PrayerBook;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.entity.skill.SkillManager;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.position.Area;
import com.osroyale.net.packet.out.SendItemOnInterface;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.net.packet.out.SendTooltip;

/**
 * Handles managing the preset system.
 *
 * @author Daniel
 */
public class PresetManager {

    /** The total preset size. */
    public static final int SIZE = 10;

    /** The player instance. */
    private final Player player;

    /** The presets. */
    public Preset[] preset = new Preset[SIZE];

    /** The preset opening on death flag. */
    public boolean deathOpen;

    /** The automatically deposit items on activate flag. */
    public boolean autoDeposit;

    /** If the player is allowed all presets. */
    private boolean permitted;

    /** Constructs a new <code>PresetManager<code>. */
    public PresetManager(Player player) {
        this.player = player;
    }

    /** Opens the preset to the last viewed slot. */
    public void open() {
        open(getSlot());
    }

    /** Opens the preset to a specific slot. */
    public void open(int slot) {
        if (valid(true)) {
            permitted = slot <= PlayerRight.getPresetAmount(player);

            if (!permitted) {
                player.dialogueFactory.sendStatement("You need to be a donator for more preset slots!").execute();
                return;
            }

            player.attributes.set("PRELOADING_SLOT_KEY", slot);
            refresh();
            player.interfaceManager.open(57_000);
        }
    }

    /** Refreshes all the components on the interface. */
    public void refresh() {
        int slot = getSlot();
        int string = 57049;
        String name;
        for (int i = 0; i < SIZE; i++) {
            boolean locked = !permitted && i >= 5;
            String defaultName = locked ? "<col=F23030>Locked" : (preset[i] == null ? "Slot " + (i + 1) : preset[i].getName());
            name = (i == slot ? "<col=ffffff>" : "<col=39BF5B>") + defaultName;
            player.send(new SendString(name, string));
            player.send(new SendTooltip("View " + name, string - 3));
            string += 4;
        }
        for (int index = 0; index < 7; index++) {
            player.send(new SendString(preset[slot] == null ? (index == 3 ? "10" : "1") : ((preset[slot].getSkill()[index] == 0 ? (index == 3 ? 10 : 1) : preset[slot].getSkill()[index])), 57014 + index));
        }
        int attack = preset[slot] == null ? 1 : preset[slot].getSkill()[Skill.ATTACK];
        int strength = preset[slot] == null ? 1 : preset[slot].getSkill()[Skill.STRENGTH];
        int defence = preset[slot] == null ? 1 : preset[slot].getSkill()[Skill.DEFENCE];
        int hitpoints = preset[slot] == null ? 10 : preset[slot].getSkill()[Skill.HITPOINTS];
        int prayer = preset[slot] == null ? 1 : preset[slot].getSkill()[Skill.PRAYER];
        int ranged = preset[slot] == null ? 1 : preset[slot].getSkill()[Skill.RANGED];
        int magic = preset[slot] == null ? 1 : preset[slot].getSkill()[Skill.MAGIC];
        double level = SkillManager.calculateCombat(attack, defence, strength, hitpoints, prayer, ranged, magic);
        player.send(new SendString("Lvl: " + level, 57041));
        boolean prayers = preset[slot] == null || preset[slot].getPrayer() == null;
        player.send(new SendString("<col=99E823>" + (prayers ? "Not " : "") + "Set!", 57013));
        player.send(new SendString("<col=BD23E8>" + (preset[slot] == null ? "Not set" : preset[slot].getSpellbook().getName()), 57011));
        player.send(new SendString("Available " + getTaken() + "/" + SIZE, 57024));
        player.send(new SendItemOnInterface(57023, preset[slot] == null ? new Item[0] : preset[slot].getInventory()));
        player.send(new SendItemOnInterface(57022, preset[slot] == null ? new Item[0] : preset[slot].getEquipment()));
    }

    /** Handles naming the preset. */
    public void name(String name) {
        int slot = getSlot();

        if (!permitted && slot >= 5) {
            player.dialogueFactory.sendStatement("You need to be a donator for more preset slots!").execute();
            return;
        }

        if (preset[slot] == null) {
            preset[slot] = new Preset(name);
        } else {
            preset[slot].setName(name);
        }
        refresh();
        player.send(new SendMessage("You have successfully titles preset #" + (slot + 1) + " to " + name + "."));
    }

    /** Handles uploading the preset content. */
    public void upload() {
        if (!valid(false))
            return;

        int slot = getSlot();

        if (!permitted && slot >= 5) {
            player.dialogueFactory.sendStatement("You need to be a donator for more preset slots!").execute();
            return;
        }

        if (preset[slot] == null) {
            player.dialogueFactory.sendStatement("Please name your preset before uploading your gear!").execute();
            return;
        }

        player.dialogueFactory.sendOption("Upload preset", () -> {
            int[] skill = new int[7];
            for (int i = 0; i < 7; i++) {
                skill[i] = player.skills.getMaxLevel(i);
            }
            PrayerBook prayerBook = new PrayerBook();
            if (player.quickPrayers != null)
                prayerBook.only(player.quickPrayers.getEnabled().toArray(new Prayer[0]));
            preset[slot] = new Preset(preset[slot].getName(), player.inventory.toArray(), player.equipment.getEquipment(), skill, prayerBook, player.spellbook);
            player.send(new SendMessage("You have successfully uploaded your preset."));
            refresh();
            AchievementHandler.activate(player, AchievementKey.PRELOAD_SETUP, 1);
            player.dialogueFactory.clear();
        }, "Nevermind", () -> player.dialogueFactory.clear()).execute();
    }

    /** Activates the preset. */
    public void activate() {
        if (!valid(false))
            return;

        int slot = getSlot();

        if (preset[slot] == null) {
            player.send(new SendMessage("You have nothing assigned to this preset!"));
            return;
        }

        if (autoDeposit) {
            player.bank.depositeInventory(false);
            player.bank.depositeEquipment(false);
        }

        if (!player.inventory.isEmpty() || !player.equipment.isEmpty()) {
            player.send(new SendMessage("Please withdraw all items from your inventory and equipment."));
            return;
        }

        for (int index = 0; index <= 6; index++) {
            int level = preset[slot].getSkill()[index];
            if (PlayerRight.isIronman(player) && player.skills.getMaxLevel(index) >= level) {
                player.skills.setLevel(index, level);
                player.skills.setMaxLevel(index, level);
            } else if (!PlayerRight.isIronman(player)){
                player.skills.setLevel(index, level);
                player.skills.setMaxLevel(index, level);
            }
        }

        player.skills.setCombatLevel();
        player.updateFlags.add(UpdateFlag.APPEARANCE);

        equipment(preset[slot]);
        inventory(preset[slot]);

        Autocast.reset(player);
        player.prayer.reset();
        player.equipment.login();
        player.inventory.refresh();
        player.quickPrayers.only(preset[slot].getPrayer().getEnabled().toArray(new Prayer[0]));
        player.spellbook = preset[slot].getSpellbook();
        player.interfaceManager.setSidebar(Config.MAGIC_TAB,  player.spellbook.getInterfaceId());
        player.send(new SendMessage("You preset has been activated. If you are missing some items it is because you did"));
        player.send(new SendMessage("not have it in your bank."));

        if (player.pvpInstance) {
            player.playerAssistant.setValueIcon();
        }
    }

    private void equipment(Preset preset) {
        for (Item item : preset.getEquipment()) {
            if (item == null)
                continue;

            if (PlayerRight.isDeveloper(player)) {
                player.equipment.manualWear(item);
                continue;
            }

            int tabSlot = player.bank.computeIndexForId(item.getId());
            if (tabSlot <= -1)
                continue;

            int tab = player.bank.tabForSlot(tabSlot);
            if (tab <= -1)
                continue;

            int amount = player.bank.get(tabSlot).getAmount();
            if (amount <= 0)
                continue;

            if (item.getAmount() > amount) {
                item = item.copy();
                item.setAmount(amount);
            }

            if (player.bank.remove(item.unnoted(), tabSlot, false)) {
                if (!player.bank.indexOccupied(tabSlot)) {
                    player.bank.changeTabAmount(tab, -1);
                    player.bank.shift();
                }

                player.equipment.manualWear(item.copy());
            }
        }
    }

    private void inventory(Preset preset) {
        Item[] inventory = preset.getInventory();
        for (int index = 0; index < inventory.length; index++) {
            Item item = inventory[index];

            if (item == null)
                continue;

            if (PlayerRight.isDeveloper(player)) {
                player.inventory.add(item, index, false);
                continue;
            }

            int tabSlot = player.bank.computeIndexForId(item.getId());
            if (tabSlot <= -1)
                continue;

            int tab = player.bank.tabForSlot(tabSlot);
            if (tab <= -1)
                continue;

            int amount = player.bank.get(tabSlot).getAmount();
            if (amount <= 0)
                continue;

            if (item.getAmount() > amount) {
                item = item.copy();
                item.setAmount(amount);
            }

            if (player.bank.remove(item.unnoted(), tabSlot, false)) {
                if (!player.bank.indexOccupied(tabSlot)) {
                    player.bank.changeTabAmount(tab, -1);
                    player.bank.shift();
                }

                player.inventory.set(index, item.copy(), false);
            }
        }
    }

    /** Handles deleting the preset. */
    public void delete() {
        if (!valid(false))
            return;
        int slot = getSlot();
        if (preset[slot] == null)
            return;
        player.dialogueFactory.sendOption("Delete " + preset[slot].getName() + " (<col=f44259>CANT BE UNDONE</col>)", () -> {
            preset[slot] = null;
            refresh();
            player.send(new SendMessage("You have successfully cleared preset #" + (slot + 1) + "."));
            player.dialogueFactory.clear();
        }, "Nevermind", () -> player.dialogueFactory.clear()).execute();
    }

    /** Handles opening the settings menu for the preset. */
    public void openSettings() {
        DialogueFactory factory = player.dialogueFactory;
        factory.sendOption("View current settings", () -> {
            factory.onAction(() -> {
                factory.sendInformationBox("<col=3E74B8>Preset Settings", "</col>Open on death: " + (deathOpen ? "<col=0E8716>Enabled" : "<col=F01818>Disabled"), "</col>Automatic deposit: " + (autoDeposit ? "<col=0E8716>Enabled" : "<col=F01818>Disabled")).execute();
            });
        }, "Toggle open on death", () -> factory.onAction(() -> {
            deathOpen = !deathOpen;
            factory.sendStatement("The preset interface will now " + (deathOpen ? "" : "<col=F01818>not</col> ") + "open on death", "automatically.").execute();
        }), "Toggle automatic deposit", () -> {
            factory.onAction(() -> {
                autoDeposit = !autoDeposit;
                factory.sendStatement("Your items will now " + (autoDeposit ? "" : "<col=F01818>not</col> ") + "automatically deposit on activation.").execute();
            });
        }, "Nevermind", () -> factory.onAction(() -> factory.clear())).execute();
    }

    /** Checks if the preset is valid. */
    private boolean valid(boolean open) {
        if (!open && !player.interfaceManager.isInterfaceOpen(57_000))
            return false;
        if (player.getCombat().inCombat()) {
            player.send(new SendMessage("You can not do this while in combat!"));
            return false;
        }

        boolean permission = false;

        if (Area.inEdgeville(player) || Area.inDonatorZone(player)) {
            permission = true;
        }

        if (!permission) {
            player.send(new SendMessage("You can only manage your preset in Edgeville or the donator zone!"));
            return false;
        }
        return !player.positionChange;
    }

    /** Gets the taken preset amount. */
    private int getTaken() {
        int taken = SIZE;
        for (int index = 0; index < SIZE; index++) {
            if (preset[index] != null || !permitted && index >= 5)
                taken--;
        }
        return taken;
    }

    /** Gets the current slot of the preset. */
    private int getSlot() {
        return player.attributes.get("PRELOADING_SLOT_KEY", Integer.class);
    }
}
