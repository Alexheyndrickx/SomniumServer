package com.osroyale.content.skill.impl.mining;

/**
 * Holds all the ore data.
 *
 * @author Daniel
 */
public enum OreData {
    CLAY(434, 1, 5, 7468, 15, 741_600, 5, 0.3, new int[]{7454, 7487}),
    COPPER(436, 1, 17.5, 7468, 15, 741_600, 5, 0.5, new int[]{7484, 7453}),
    TIN(438, 1, 17.5, 7468, 15, 741_600, 5, 0.5, new int[]{7485, 7486}),
    IRON(440, 15, 35, 7468, 20, 741_600, 8, 0.2, new int[]{7488, 7455}),
    SILVER(442, 20, 40, 7468, 25, 741_600, 8, 0.2, new int[]{7457, 7490}),
    COAL(453, 30, 50, 7468, 25, 290_640, 9, 0.2, new int[]{7456, 7489}),
    GOLD(444, 40, 65, 7468, 30, 296_640, 7, 0.15, new int[]{7491, 7458}),
    MITHRIL(447, 55, 80, 7468, 50, 148_320, 9, 0.05, new int[]{7492, 7459}),
    ADAMANTITE(449, 70, 95, 7468, 75, 59_328, 11, 0.03, new int[]{7493, 7460}),
    RUNITE(451, 85, 125, 7468, 100, 42_337, 13, 0.02, new int[]{7494}),
    RUNE_ESSENCE(7936, 1, 25, -1, 20, 326_780, Integer.MAX_VALUE, 0.1, new int[]{7494,7471}),
    GEM_ROCK(-1, 40, 85, -1, 75, 326_780, 2, 0.2, new int[] {9030}),
    GEM_ROCK_I(-1, 40, 65, 7468, 135, 326_780, 2, 0.3, new int[] {7463,7464}),
    ;

    /** The ore this node contains. */
    public final int ore;

    /** The minimum level to mine this node. */
    public final int level;

    /** The experience. */
    public final double experience;

    /** The node replacement. */
    public final int replacement;

    /** The node respawn timer. */
    public final int respawn;

    /** The pet chance. */
    public final int pet;

    /** The amount of ores that this ore can give. */
    public final int oreCount;

    /** The success rate for the ore data. */
    public final double success;

    /** The object identification of this node. */
    public final int[] objects;

    /** Creates the node. */
    OreData(int ore, int level, double experience, int replacement, int respawn, int pet, int oreCount, double success, int[] objects) {
        this.objects = objects;
        this.level = level;
        this.experience = experience;
        this.replacement = replacement;
        this.respawn = respawn;
        this.pet = pet;
        this.oreCount = oreCount;
        this.success = success;
        this.ore = ore;
    }

    /** Gets the ore data. */
    public static OreData forId(int id) {
        for (OreData data : OreData.values())
            for (int object : data.objects)
                if (object == id)
                    return data;
        return null;
    }
}
