package com.osroyale.content.skill.impl.farming;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.osroyale.content.skill.impl.farming.zones.*;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObject;

import java.util.LinkedList;
import java.util.List;

public class Farming {
    private final List<FarmingZone> zones = new LinkedList<>();

    public Farming(Player player) {
        zones.add(new CatherbyZone(player));
        zones.add(new ArdougneZone(player));
        zones.add(new FaladorZone(player));
        zones.add(new PhasmatyZone(player));
    }

    public static boolean itemOnObject(Player player, GameObject object, Item item, int slot) {
        boolean success = false;
        for (FarmingZone zone : player.farming.zones) {
            if (zone.isViewable(player.getPosition())) {
                success |= zone.itemOnObject(object, item, slot);
            }
        }
        return success;
    }

    public static boolean firstClickObject(Player player, GameObject object) {
        boolean success = false;
        for (FarmingZone zone : player.farming.zones) {
            if (zone.isViewable(player.getPosition())) {
                success |= zone.clickObject(object, 1);
            }
        }
        return success;
    }

    public static boolean secondClickObject(Player player, GameObject object) {
        boolean success = false;
        for (FarmingZone zone : player.farming.zones) {
            if (zone.isViewable(player.getPosition())) {
                success |= zone.clickObject(object, 2);
            }
        }
        return success;
    }

    public static void tick(Player player) {
        player.farming.zones.forEach(FarmingZone::tick);
    }

    public void regionChange(Player player) {
        zones.forEach(zone -> zone.sendPatchConfigs(player));
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        for (FarmingZone zone : zones) {
            object.add(zone.getClass().getSimpleName(), zone.toJson());
        }
        return object;
    }

    public void fromJson(JsonObject object) {
        for (FarmingZone zone : zones) {
            if (!object.has(zone.getClass().getSimpleName())) {
                continue;
            }

            JsonArray thing = object.get(zone.getClass().getSimpleName()).getAsJsonArray();
            zone.fromJson(thing);
        }
    }

}