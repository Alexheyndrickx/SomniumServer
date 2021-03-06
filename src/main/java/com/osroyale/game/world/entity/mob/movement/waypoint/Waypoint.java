package com.osroyale.game.world.entity.mob.movement.waypoint;

import com.osroyale.game.task.Task;
import com.osroyale.game.world.Interactable;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.data.PacketType;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;
import com.osroyale.game.world.region.Region;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.Utility;

import java.util.Objects;

public abstract class Waypoint extends Task {
    protected final Mob mob;
    protected Interactable target;
    private Position lastPosition;

    protected Waypoint(Mob mob, Interactable target) {
        super(true, 0);
        this.mob = mob;
        this.target = target;
    }

    protected abstract void onDestination();

    protected int getRadius() {
        return 1;
    }

    protected boolean withinDistance() {
        return Utility.getDistance(mob, target) <= getRadius() && !mob.movement.needsPlacement();
    }

    @Override
    protected void onSchedule() {
        if (mob.locking.locked(PacketType.MOVEMENT)) {
            return;
        }

        if (target instanceof Mob) {
            mob.interact((Mob) target);
        }

        if (!withinDistance()) {
            findRoute();
        }
    }

    @Override
    public void execute() {
        if (Utility.inside(mob, target) && target instanceof Mob) {
            if (!mob.locking.locked(PacketType.MOVEMENT) && !mob.movement.needsPlacement())
                Utility.fixInsidePosition(mob, target);
            return;
        }

        if (withinDistance()) {
            onDestination();
            return;
        }

        if (target.getPosition().equals(lastPosition)) {
            return;
        }

        if (mob.locking.locked(PacketType.MOVEMENT)) {
            return;
        }

        lastPosition = target.getPosition();
        findRoute();
    }

    private void findRoute() {
        if (target instanceof Player && mob.equals(((Player) target).pet)) {
            int distance = Utility.getDistance(mob, target);
            if (distance > Region.VIEW_DISTANCE) {
                Npc pet = mob.getNpc();
                pet.move(target.getPosition());
                pet.instance = ((Player) target).instance;
                World.schedule(1, () -> {
                    pet.interact((Player) target);
                    pet.follow((Player) target);
                });
            }
        }

//        if (this instanceof CombatWaypoint) {
//            System.out.println(mob.getPosition());
//            System.out.println(target.getPosition());
//            System.out.println(Utility.getDelta(mob, target));
//            System.out.println();
//        }

        boolean smart = mob.isPlayer() || (mob.isNpc() && !(this instanceof CombatWaypoint));

        if (smart && mob.movement.dijkstraPath(target)) {
            return;
        }

        if (mob.movement.simplePath(target)) {
            return;
        }

        if (mob.isPlayer())
            mob.getPlayer().send(new SendMessage("I can't reach that!"));

        /* No path can be found, lets get out of here!!!! */
        cancel();
    }

    @Override
    protected void onCancel(boolean logout) {
        mob.resetFace();

        if (target instanceof Mob) {
            Mob other = (Mob) target;
            other.attributes.remove("mob-following");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Waypoint) {
            Waypoint other = (Waypoint) obj;
            return Objects.equals(mob, other.mob)
                && Objects.equals(target, other.target);
        }
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "target=" + target +
                '}';
    }

    public Interactable getTarget() {
        return target;
    }

    public void onChange() {
        execute();
//        mob.movement.processNextMovement();
    }
}
