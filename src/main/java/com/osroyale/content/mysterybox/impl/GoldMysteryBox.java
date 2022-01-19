package com.osroyale.content.mysterybox.impl;

import com.osroyale.content.mysterybox.MysteryBox;
import com.osroyale.content.mysterybox.MysteryItem;

import static com.osroyale.content.mysterybox.MysteryRarity.COMMON;
import static com.osroyale.content.mysterybox.MysteryRarity.EXOTIC;
import static com.osroyale.content.mysterybox.MysteryRarity.RARE;

/**
 * The gold (tier 3) mystery box.
 *
 * @author Daniel
 */
public class GoldMysteryBox extends MysteryBox {
    @Override
    protected String name() {
        return "Gold mystery box";
    }

    @Override
    protected int item() {
        return 11739;
    }

    @Override
    protected MysteryItem[] rewards() {
        return new MysteryItem[]{
                new MysteryItem(995, 10000000, COMMON), // COINS
                new MysteryItem(11840, 1, COMMON), // DRAGON_BOOTS
                new MysteryItem(11335, 1, COMMON), // DRAGON_FULL_HELM
                new MysteryItem(11920, 1, COMMON), // DRAGON_PICKAXE
                new MysteryItem(6739, 1, COMMON), // DRAGON_AXE
                new MysteryItem(12881, 1, COMMON), // AHRIM'S_SET
                new MysteryItem(12877, 1, COMMON), // DHAROK'S_SET
                new MysteryItem(12873, 1, COMMON), // GUTHAN'S_SET
                new MysteryItem(12883, 1, COMMON), // KARIL'S_SET
                new MysteryItem(12879, 1, COMMON), // TORAG'S_SET
                new MysteryItem(12875, 1, COMMON), // VERAC'S_SET
                new MysteryItem(11907, 1, COMMON), // TRIDENT_OF_THE_SEAS
                new MysteryItem(6731, 1, COMMON), // SEERS_RING
                new MysteryItem(6733, 1, COMMON), // ARCHERS_RING
                new MysteryItem(6735, 1, COMMON), // WARRIOR_RING
                new MysteryItem(6737, 1, COMMON), // BERSERKER_RING
                new MysteryItem(11791, 1, COMMON), // STAFF_OF_THE_DEAD
                new MysteryItem(11824, 1, COMMON), // ZAMORAKIAN_SPEAR
                new MysteryItem(11235, 1, COMMON), // DARK_BOW
                new MysteryItem(4151, 1, COMMON), // ABYSSAL_WHIP
                new MysteryItem(6585, 1, COMMON), // AMULET_OF_FURY
                new MysteryItem(12002, 1, COMMON), // OCCULT_NECKLACE

                new MysteryItem(2581, 1, RARE), // ROBIN_HAT
                new MysteryItem(12596, 1, RARE), // RANGER_TUNIC
                new MysteryItem(19994, 1, RARE), // RANGER_GLOVE
                new MysteryItem(2577, 1, RARE), // RANGER_BOOT
                new MysteryItem(11926, 1, RARE), // ODIUM_WARD
                new MysteryItem(11924, 1, RARE), // MALEDICTION_WARD
                new MysteryItem(12924, 1, RARE), // TOXIC_BLOWPIPE
                new MysteryItem(11804, 1, RARE), // BANDOS_GODSWORD
                new MysteryItem(11806, 1, RARE), // SARADOMIN_GODSWORD
                new MysteryItem(11808, 1, RARE), // ZAMORAK_GODSWORD
                new MysteryItem(19481, 1, RARE), // HEAVY_BALISTA
                new MysteryItem(12904, 1, RARE), // TOXIC_STAFF_OF_THE_DEAD
                new MysteryItem(12006, 1, RARE), // ABYSSAL_TENTACLE
                new MysteryItem(13263, 1, RARE), // ABYSSAL_BLUDGEON
                new MysteryItem(13265, 1, RARE), // ABYSSAL_DAGGER
                //ADD DRAGONBONE NECKLACE if you want here too

                new MysteryItem(11832, 1, EXOTIC), // BANDOS_CHESTPLATE
                new MysteryItem(11834, 1, EXOTIC), // BANDOS_TASSETS
                new MysteryItem(11828, 1, EXOTIC), // ARMADYL_CHESTPLATE
                new MysteryItem(11830, 1, EXOTIC), // ARMADYL_CHAINSKIRT
                new MysteryItem(11785, 1, EXOTIC), // ARMADYL_CROSSBOW
                new MysteryItem(11802, 1, EXOTIC), // ARMADYL_GODSWORD
                new MysteryItem(21633, 1, EXOTIC), // ANCIENT_WYVERN_SHIELD
                new MysteryItem(12821, 1, EXOTIC), // SPECTERAL_SPIRIT SHIELD
                new MysteryItem(19547, 1, EXOTIC), // NECKLACE_OF_ANGUISH
                new MysteryItem(19553, 1, EXOTIC), // AMULET_OF_TORTURE
                new MysteryItem(19544, 1, EXOTIC), // TORMENTED_BRACELET
                new MysteryItem(13576, 1, EXOTIC), // DRAGON_WARHAMMER
                new MysteryItem(13235, 1, EXOTIC), // ETERNAL_BOOTS
                new MysteryItem(13239, 1, EXOTIC), // PRIMORDIAL_BOOTS
                new MysteryItem(13237, 1, EXOTIC), // PEGASIAN_BOOTS
                new MysteryItem(11739, 1, EXOTIC), // GOLD_MBOX
                new MysteryItem(8038, 1, EXOTIC), // PET_BOX
                //ADD Dragon Platebody/Kiteshield if you want here too
        };
    }
}
