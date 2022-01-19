package com.osroyale.content.mysterybox.impl;

import com.osroyale.content.mysterybox.MysteryBox;
import com.osroyale.content.mysterybox.MysteryItem;
import com.osroyale.game.world.items.Item;

import static com.osroyale.content.mysterybox.MysteryRarity.EXOTIC;
import static com.osroyale.content.mysterybox.MysteryRarity.RARE;

/**
 * The silver (tier 2) mystery box.
 *
 * @author Daniel
 */
public class SilverMysteryBox extends MysteryBox {
    @Override
    protected String name() {
        return "Silver mystery box";
    }

    @Override
    protected int item() {
        return 12955;
    }

    @Override
    protected MysteryItem[] rewards() {
        return new MysteryItem[]{
                new MysteryItem(995, 50000), // COINS
                new MysteryItem(2999, 25), // TOADFLAX
                new MysteryItem(3001, 25), // SNAPDRAGON
                new MysteryItem(1704, 1), // AMULET_OF_GLORY
                new MysteryItem(1615, 1), // DRAGONSTONE
                new MysteryItem(11126, 1), // COMBAT_BRACELET
                new MysteryItem(11818, 1), // GODSWORD_SHARD_1
                new MysteryItem(11820, 1), // GODSWORD_SHARD_2
                new MysteryItem(11822, 1), // GODSWORD_SHARD_3
                new MysteryItem(1187, 1), // DRAGON_SQ_SHIELD
                new MysteryItem(4587, 1), // DRAGON_SCIMITAR
                new MysteryItem(4087, 1), // DRAGON_PLATELEGS
                new MysteryItem(4585, 1), // DRAGON_PLATESKIRT
                new MysteryItem(3204, 1), // DRAGON_HALBERD
                new MysteryItem(1249, 1), // DRAGON_SPEAR
                new MysteryItem(1305, 1), // DRAGON_LONGSWORD
                new MysteryItem(1377, 1), // DRAGON_BATTLEAXE
                new MysteryItem(7158, 1), // DRAGON_2H_SWORD
                new MysteryItem(3751, 1), // BERSERKER_HELM
                new MysteryItem(3753, 1), // WARRIOR_HELM
                new MysteryItem(3755, 1), // FARSEER_HELM
                new MysteryItem(3749, 1), // ARCHER_HELM
                new MysteryItem(6528, 1), // TZHAAR_KET_OM
                new MysteryItem(4153, 1), // GRANITE_MAUL
                new MysteryItem(4131, 1), // RUNE_BOOTS
                new MysteryItem(3122, 1), // GRANITE_SHIELD
                new MysteryItem(10551, 1), // FIGHTER_TORSO
                new MysteryItem(7462, 1), // BARROWS_GLOVES
                new MysteryItem(8850, 1), // RUNE_DEFENDER
                new MysteryItem(6570, 1), // FIRE_CAPE
                new MysteryItem(11132, 1), // REGEN_BRACELET
                new MysteryItem(3140, 1, RARE), // DRAGON_CHAINBODY
                new MysteryItem(11128, 1, RARE), // BERSERKER_NECKLACE
                new MysteryItem(11840, 1, RARE), // DRAGON_BOOTS
                new MysteryItem(11838, 1, RARE), // SARADOMIN_SWORD
                new MysteryItem(11836, 1, RARE), // BANDOS_BOOTS
                new MysteryItem(11826, 1, RARE), // ARMADYL_HELMET
                new MysteryItem(6571, 1, RARE), // UNCUT_ONYX
                new MysteryItem(4151, 1, RARE), // ABYSSAL_WHIP
                new MysteryItem(12598, 1, RARE), // HOLY_SANDALS
                new MysteryItem(2577, 1, EXOTIC), // RANGER_BOOTS
                new MysteryItem(2579, 1, EXOTIC), // WIZARD_BOOTS
                new MysteryItem(2581, 1, EXOTIC) // ROBIN_HOOD_HAT
        };
    }
}
