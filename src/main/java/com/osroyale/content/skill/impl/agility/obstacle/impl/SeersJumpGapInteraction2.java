package com.osroyale.content.skill.impl.agility.obstacle.impl;

import com.osroyale.content.skill.impl.agility.obstacle.ObstacleInteraction;
import com.osroyale.game.Animation;
import com.osroyale.game.task.Task;
import com.osroyale.game.task.impl.ForceMovementTask;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.player.ForceMovement;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;

public interface SeersJumpGapInteraction2 extends ObstacleInteraction {
    @Override
    default void start(Player player) {
//        player.getUpdateFlags().sendFaceToDirection(player.getX(), player.getY() - 1);
    }

    @Override
    default void onExecution(Player player, Position start, Position end) {
//        player.move(new Position(player.getX(), player.getY(), player.getHeight() - 1));

        ForceMovement forceMovement = new ForceMovement(player.getPosition(), new Position(player.getX(), player.getY() - 3, player.getHeight()), 12, 2, Direction.SOUTH);
        World.schedule(new ForceMovementTask(player, 0, forceMovement, new Animation(5043)) {
            @Override
            protected void onCancel(boolean logout) {
                super.onCancel(logout);
                World.schedule(new Task(1) {
                    int ticks = 0;
                    @Override
                    public void execute() {
                        switch (ticks++) {
                            case 3:
                                player.move(new Position(player.getX(), player.getY(), player.getHeight() + 1));
                                break;
                            case 4:
                                player.move(end);
                                cancel();
                                break;
                        }
                    }
                });
            }
        });
    }

    @Override
    default void onCancellation(Player player) {
    }
}
