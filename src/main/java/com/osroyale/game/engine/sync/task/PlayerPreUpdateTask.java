package com.osroyale.game.engine.sync.task;

import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.session.GameSession;
import com.osroyale.util.Stopwatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public final class PlayerPreUpdateTask extends SynchronizationTask {

	private static final Logger logger = LogManager.getLogger();

	private final Player player;

	public PlayerPreUpdateTask(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {

			try {
				player.movement.processNextMovement();
			} catch (Exception ex) {
				logger.error(String.format("error player.movement.processNextMovement(): %s", player), ex);
			}

//				elapsed = stopwatch.elapsedTime(TimeUnit.MILLISECONDS);
//				if (elapsed > 0) {
//					System.out.println(String.format("processNextMovement: %d", elapsed));
//				}
//				stopwatch.reset();



//				elapsed = stopwatch.elapsedTime(TimeUnit.MILLISECONDS);
//				if (elapsed > 0) {
//					System.out.println(String.format("sequence: %d", elapsed));
//				}
//				stopwatch.reset();
		} catch (Exception ex) {
			logger.error(String.format("Error in %s.", PlayerPreUpdateTask.class.getSimpleName()), ex);
		}
	}

}
