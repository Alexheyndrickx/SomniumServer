package com.osroyale.game.world.entity.combat.strategy.basic;

import com.osroyale.Config;
import com.osroyale.game.world.entity.combat.attack.FightType;
import com.osroyale.game.world.entity.combat.hit.Hit;
import com.osroyale.game.world.entity.combat.strategy.CombatStrategy;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.pathfinding.path.SimplePathChecker;
import com.osroyale.util.Utility;

/**
 * @author Michael | Chex
 */
public abstract class MagicStrategy<T extends Mob> extends CombatStrategy<T> {

    @Override
    public boolean withinDistance(T attacker, Mob defender) {
        FightType fightType = attacker.getCombat().getFightType();
        int distance = getAttackDistance(attacker, fightType);

        if (attacker.movement.needsPlacement() && defender.movement.needsPlacement()
                && !attacker.locking.locked()) {
            distance++;
            if (defender.movement.isRunning())
                distance++;
        }

        return Utility.within(attacker, defender, distance) && (SimplePathChecker.checkProjectile(attacker, defender) || SimplePathChecker.checkProjectile(defender, attacker));
    }

    protected static void addCombatExperience(Player player, double base, Hit... hits) {
        int exp = 0;
        for (Hit hit : hits) {
            if (hit.getDamage() <= 0) continue;
            exp += hit.getDamage();
        }

        exp *= Config.COMBAT_MODIFICATION;
        exp += base;
        player.skills.addExperience(Skill.MAGIC, exp);
        player.skills.addExperience(Skill.HITPOINTS, exp / 3);
    }

}
