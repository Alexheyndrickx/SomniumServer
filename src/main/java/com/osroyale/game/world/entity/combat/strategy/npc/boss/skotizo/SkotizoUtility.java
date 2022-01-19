package com.osroyale.game.world.entity.combat.strategy.npc.boss.skotizo;

import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.Direction;
import com.osroyale.game.world.entity.mob.Mob;
import com.osroyale.game.world.entity.mob.npc.Npc;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.discord.Discord;
import com.osroyale.util.Utility;

/**
 * Created by Daniel on 2017-12-20.
 */
public class SkotizoUtility {

    static Npc skotizo = null;
    private static SpawnData data;

    static Npc generateSpawn() {
        data = SpawnData.generate();
        skotizo = new Npc(7286, data.position, 10, Mob.DEFAULT_INSTANCE, Direction.NORTH);
        skotizo.register();
        skotizo.definition.setRespawnTime(-1);
        skotizo.definition.setAggressive(true);
        skotizo.speak("Darkness is here to penetrate your souls!");
        World.sendMessage("<icon=6><col=8714E6> Skotizo has just spawned! He is located at " + data.location + "!", "<icon=6><col=8714E6> First clan to kill him will be rewarded handsomely!");
//        Discord.message("Skotizo has entered the wilderness! He is located at " + data.location + "!");
        return skotizo;
    }

    public static String getInformation() {
        return (skotizo == null || skotizo.isDead()) ? "Not Active" : data.location;
    }

    public static void defeated(Npc npc, Player player) {
        boolean hasClan = player.clanChannel != null;

        if (hasClan) {
            player.clanChannel.getDetails().points += 5;
            player.clanChannel.addExperience(10000);
            World.sendMessage("<icon=6><col=8714E6> Skotizo has been defeated by " + player.getName() + ", a clan member of " + player.clanChannel.getName() + "!");
            player.clanChannel.message("Hell yeah boys! We just killed Skotizo!! We earned 10,000 EXP & 5 CP.");
//            Discord.message("Skotizo has been killed by the " + player.clanChannel.getName() + " clan!");
        } else {
            World.sendMessage("<icon=6><col=8714E6> Skotizo has been defeated by " + player.getName() + ", a solo individual with balls of steel!");
//            Discord.message("Skotizo has been killed by the " + player.getPosition() + "!");
        }

        if (skotizo != null && skotizo.isRegistered()) {
            skotizo.unregister();
        }

        skotizo = null;
    }

    public enum SpawnData {
        LEVEL_18("Near east dragons", new Position(3307, 3668, 0)),
        LEVEL_19("Near obelisk", new Position(3222, 3658, 0)),
        LEVEL_28("Near vennenatis", new Position(3308, 3737, 0)),
        LEVEL_41("Near callisto", new Position(3270, 3843, 0)),
        LEVEL_52("Near obelisk", new Position(3304, 3929, 0)),
        LEVEL_53("Near scorpia's cave", new Position(3211, 3944, 0));

        public final String location;
        public final Position position;

        SpawnData(String location, Position position) {
            this.location = location;
            this.position = position;
        }

        public static SpawnData generate() {
            return Utility.randomElement(values());
        }
    }
}
