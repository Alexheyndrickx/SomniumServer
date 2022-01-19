package com.osroyale.game.world.entity.mob.npc.drop;

import com.osroyale.util.Utility;

/**
 * The enumerated type whose elements represent a set of constants used to differ between
 * {@link NpcDrop} rarities.
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 29-1-2017.
 */
public enum NpcDropChance {
    ALWAYS(1, 0),
    COMMON(2, 10),
    UNCOMMON(3, 25),
    RARE(4, 260),
    VERY_RARE(5, 320);

    /** The tier of this drop chance.  */
    public final int tier;

    /**  The denominator which is the divisor of a nominator of 1.  */
    public final int denominator;

    /** Constructs a new {@link NpcDropChance}. */
    NpcDropChance(int tier, int denominator) {
        this.denominator = denominator;
        this.tier = tier;
    }

    @Override
    public String toString() {
        return Utility.formatEnum(name());
    }
}
