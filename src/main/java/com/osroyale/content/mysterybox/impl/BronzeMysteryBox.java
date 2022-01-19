package com.osroyale.content.mysterybox.impl;

import com.osroyale.content.mysterybox.MysteryBox;
import com.osroyale.content.mysterybox.MysteryItem;

import static com.osroyale.content.mysterybox.MysteryRarity.EXOTIC;
import static com.osroyale.content.mysterybox.MysteryRarity.RARE;

/**
 * The bronze (tier 1) mystery box.
 *
 * @author Daniel
 */
public class BronzeMysteryBox extends MysteryBox {
    @Override
    protected String name() {
        return "Bronze mystery box";
    }

    @Override
    protected int item() {
        return 6199;
    }
    @Override
    protected MysteryItem[] rewards() {
        return new MysteryItem[]{
                new MysteryItem(995, 30000), // COINS
                new MysteryItem(392, 100), // MANTA_RAY
                new MysteryItem(1163, 1), // RUNE_FULL_HELM
                new MysteryItem(1127, 1), // RUNE_PLATEBODY
                new MysteryItem(1093, 1), // RUNE_PLATESKIRT
                new MysteryItem(1201, 1), // RUNE_KITESHIELD
                new MysteryItem(1079, 1), // RUNE_PLATELEGS
                new MysteryItem(1333, 1), // RUNE_SCIMITAR
                new MysteryItem(1319, 1), // RUNE_2HANDED_SWORD
                new MysteryItem(1187, 1), // DRAGON_SQ_SHIELD
                new MysteryItem(4587, 1), // DRAGON_SCIMITAR
                new MysteryItem(4087, 1), // DRAGON_PLATELEGS
                new MysteryItem(4585, 1), // DRAGON_PLATESKIRT
                new MysteryItem(3204, 1), // DRAGON_HALBERD
                new MysteryItem(1249, 1), // DRAGON_SPEAR
                new MysteryItem(1305, 1), // DRAGON_LONGSWORD
                new MysteryItem(1377, 1), // DRAGON_BATTLEAXE
                new MysteryItem(7158, 1), // DRAGON_2H_SWORD
                new MysteryItem(10828, 1), // HELM_OF_NEIT
                new MysteryItem(4153, 1), // GRANITE_MAUL
                new MysteryItem(3122, 1), // GRANITE_SHIELD
                new MysteryItem(10564, 1), // GRANITE_CHEST
                new MysteryItem(6528, 1), // TZHAAR_KET_OM
                new MysteryItem(4675, 1), // ANCIENT_STAFF
                new MysteryItem(4089, 1), // MYSTIC_HAT
                new MysteryItem(4091, 1), // MYSTIC_TOP
                new MysteryItem(4093, 1), // MYSTIC_BOTTOM
                new MysteryItem(4097, 1), // MYSTIC_HAT
                new MysteryItem(10551, 1, RARE), // FIGHTER_TORSO
                new MysteryItem(7462, 1, RARE), // BARROWS_GLOVES
                new MysteryItem(8850, 1, RARE), // RUNE_DEFENDER
                new MysteryItem(6570, 1, RARE), // FIRE_CAPE
                new MysteryItem(6571, 1, EXOTIC), // UNCUT_ONYX
                new MysteryItem(4151, 1, EXOTIC), // ABYSSAL_WHIP
                new MysteryItem(2577, 1, EXOTIC) // RANGER_BOOT
        };
    }
}
