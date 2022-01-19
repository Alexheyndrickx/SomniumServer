package com.osroyale.game.world.entity.combat.projectile;

import com.osroyale.game.Animation;
import com.osroyale.game.Graphic;
import com.osroyale.game.Projectile;
import com.osroyale.game.world.entity.combat.CombatImpact;
import com.osroyale.game.world.entity.mob.Mob;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

public final class CombatProjectile {

    public static final Map<String, CombatProjectile> definitions = new HashMap<>();

    private final String name;
    private final int maxHit;
    private final CombatImpact effect;
    private final Animation animation;
    private final Graphic start;
    private final Graphic end;
    private final Projectile projectile;

    private final int hitDelay;
    private final int hitsplatDelay;

    public CombatProjectile(String name, int maxHit, CombatImpact effect, Animation animation, Graphic start, Graphic end, Projectile projectile, int hitDelay, int hitsplatDelay) {
        this.name = name;
        this.maxHit = maxHit;
        this.effect = effect;
        this.animation = animation;
        this.start = start;
        this.end = end;
        this.projectile = projectile;
        this.hitDelay = hitDelay;
        this.hitsplatDelay = hitsplatDelay;
    }

    public void sendProjectile(Mob attacker, Mob defender) {
        if (projectile != null) {
            projectile.send(attacker, defender);
        }
    }

    public static CombatProjectile getDefinition(String name) {
        return definitions.get(name);
    }

    public String getName() {
        return name;
    }

    public int getMaxHit() {
        return maxHit;
    }

    public Optional<CombatImpact> getEffect() {
        return Optional.ofNullable(effect);
    }

    public Optional<Animation> getAnimation() {
        return Optional.ofNullable(animation);
    }

    public Optional<Graphic> getStart() {
        return Optional.ofNullable(start);
    }

    public Optional<Graphic> getEnd() {
        return Optional.ofNullable(end);
    }

    public OptionalInt getHitDelay() {
        if (hitDelay == -1) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(hitDelay);
    }

    public OptionalInt getHitsplatDelay() {
        if (hitsplatDelay == -1) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(hitsplatDelay);
    }

    public Optional<Projectile> getProjectile() {
        return Optional.ofNullable(projectile);
    }

    public static final class ProjectileBuilder {
        public short id;
        public byte delay;
        public byte speed;
        public byte startHeight;
        public byte endHeight;
    }

}
