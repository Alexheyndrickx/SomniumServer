package com.osroyale.content.bloodmoney;

import com.osroyale.Config;
import com.osroyale.game.task.Task;
import com.osroyale.game.world.World;
import com.osroyale.game.world.WorldType;
import com.osroyale.util.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * Handles the blood chest event.
 *
 * @author Daniel
 */
public class BloodChestEvent extends Task {

    /** Constructs a new <code>BloodChestEvent</code>. */
    public BloodChestEvent() {
        super(100);
    }

    @Override
    public void execute() {
        if (!BloodMoneyChest.active) {
            if (Config.WORLD_TYPE == WorldType.LIVE && World.getPlayerCount() < 35) {
                return;
            }
            if (BloodMoneyChest.stopwatch.elapsedTime(TimeUnit.MINUTES) == (Config.WORLD_TYPE == WorldType.LIVE ? 45 : 5)) {
                BloodMoneyChest.spawn();
                BloodMoneyChest.stopwatch.reset();
            }
            return;
        }

        if (BloodMoneyChest.stopwatch.elapsedTime(TimeUnit.MINUTES) == 10) {//10
            BloodMoneyChest.finish(false);
        } else if (BloodMoneyChest.stopwatch.elapsedTime(TimeUnit.MINUTES) == 5) {//5
            World.sendMessage("<icon=0><col=FF0000> Blood money chest will vanish in 5 minutes!");
        }
    }
}
