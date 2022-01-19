package com.osroyale.content.mysterybox;

import com.osroyale.game.task.TickableTask;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.net.packet.out.SendColor;
import com.osroyale.net.packet.out.SendConfig;
import com.osroyale.net.packet.out.SendItemOnInterfaceSlot;
import com.osroyale.net.packet.out.SendString;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MysteryBoxEvent extends TickableTask {
    private final Player player;
    private List<MysteryItem> items;
    private List<MysteryItem> allItems;
    private int speed;
    private final MysteryBoxManager mysteryBox;

    MysteryBoxEvent(Player player) {
        super(false, 1);
        this.player = player;
        this.items = new ArrayList<>();
        this.allItems = new ArrayList<>();
        this.speed = Utility.random(8, 12);
        this.mysteryBox = player.mysteryBox;
    }

    private void move() {
        allItems.add(items.get(0));
        items.remove(0);
        MysteryItem next = getNextItem();
        allItems.remove(next);
        items.add(next);
    }

    private MysteryItem getNextItem() {
        MysteryItem next = null;
        for (MysteryItem item : allItems) {
            if (!items.contains(item)) {
                next = item;
                break;
            }
        }
        return next;
    }

    private void reward() {
        MysteryItem reward = items.get(5);
        String boxName = mysteryBox.box.name();
        MysteryItem newItem = reward;

        if (reward.getAmount() > 1 && reward.isNoteable()) {
            newItem = new MysteryItem(reward.getNotedId(), reward.getAmount(), reward.rarity);
        }

        byte type = getType(reward);

        if (type != 1) {
            World.sendMessage("<icon=17><col=5739B3> OSR: <col=" + player.right.getColor() + ">" + player.getName() + " </col>has won " + Utility.getAOrAn(reward.getName()) + " <col=5739B3>" + reward.getName() + " </col>from the <col=5739B3>" + boxName + "</col>.");
        }

        player.locking.unlock();
        player.inventory.add(newItem);
        player.dialogueFactory.sendItem(boxName, "Congratulations, you have won " + Utility.getAOrAn(reward.getName()) + " " + reward.getName() + "!", reward.getId()).execute();
        player.send(new SendColor(59508, 0x37991C));
    }

    @Override
    protected boolean canSchedule() {
        return player.mysteryBox.box != null;
    }

    @Override
    protected void onSchedule() {
        player.dialogueFactory.clear();
        player.locking.lock();
        allItems.addAll(Arrays.asList(mysteryBox.box.rewards()));
        player.inventory.remove(mysteryBox.box.item(), 1);
        mysteryBox.count = player.inventory.computeAmountForId(mysteryBox.box.item());
        Collections.shuffle(allItems);

        for (int index = 0; index < 11; index++) {
            if (index >= allItems.size())
                continue;
            MysteryItem item = allItems.get(index);
            items.add(item);
            allItems.remove(index);
        }

        player.send(new SendColor(59508, 0xF01616));
        player.send(new SendString("You have " + mysteryBox.count + " mystery box available!", 59507));

    }

    private byte getType(MysteryItem item) {
        byte type = 1;
        if (item.rarity == MysteryRarity.EXOTIC) {
            type = 2;
        } else if (item.rarity == MysteryRarity.RARE) {
            type = 0;
        }
        return type;
    }

    @Override
    protected void tick() {
        move();

        for (int index = 0; index < 11; index++) {
            if (index >= items.size())
                continue;
            MysteryItem item = items.get(index);

            player.send(new SendConfig((430 + index), getType(item)));
            player.send(new SendItemOnInterfaceSlot(59512, item, index));
        }

        if (tick == speed) {
            MysteryItem reward = items.get(6);

            if (!RandomUtils.success(reward.rarity.chance)) {
                speed++;
            } else {
                setDelay(1);
            }
        }
        if (tick > speed)
            cancel();
    }

    @Override
    protected void onCancel(boolean logout) {
        if (logout) {
            player.inventory.add(mysteryBox.box.item(), 1);
        } else {
            reward();
        }
    }
}
