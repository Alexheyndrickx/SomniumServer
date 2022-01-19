package com.osroyale.content.skill.impl.mining;

import com.osroyale.Config;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.achievement.AchievementKey;
import com.osroyale.content.activity.randomevent.RandomEventHandler;
import com.osroyale.content.clanchannel.content.ClanTaskKey;
import com.osroyale.content.pet.PetData;
import com.osroyale.content.pet.Pets;
import com.osroyale.content.prestige.PrestigePerk;
import com.osroyale.game.action.Action;
import com.osroyale.game.action.policy.WalkablePolicy;
import com.osroyale.game.task.impl.ObjectReplacementEvent;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.items.Item;
import com.osroyale.game.world.object.GameObject;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.RandomUtils;
import com.osroyale.util.Utility;


/**
 * Created by Daniel on 2017-12-18.
 */
public class MiningAction extends Action<Player> {
    private final GameObject object;
    private final OreData ore;
    private final PickaxeData pickaxe;

    MiningAction(Player player, GameObject object, OreData ore, PickaxeData pickaxe) {
        super(player, 3, false);
        this.object = object;
        this.ore = ore;
        this.pickaxe = pickaxe;
    }

    private boolean mine() {
        if (getMob().inventory.getFreeSlots() == 0) {
            getMob().dialogueFactory.sendStatement("You can't carry anymore ore.").execute();
            return false;
        }

        getMob().animate(pickaxe.animation);

        if (Mining.success(getMob(), ore, pickaxe)) {
            if (object == null || !object.active()) {
                return false;
            }

            int amount = object.getGenericAttributes().get("ores", Integer.class);

            if (amount == -1) {
                getMob().resetAnimation();
                return false;
            }

            int harvest = ore.ore;
            boolean gem = harvest == -1;
            object.getGenericAttributes().modify("ores", amount - 1);

            if (gem) {
                harvest = Mining.GEM_ITEMS.next().getId();
            }

            getMob().inventory.add(harvest, 1);
            getMob().skills.addExperience(Skill.MINING, ore.experience * Config.MINING_MODIFICATION);
            getMob().playerAssistant.activateSkilling(1);
            RandomEventHandler.trigger(getMob());
            Pets.onReward(getMob(), PetData.ROCK_GOLEM);

            if (ore == OreData.RUNITE) {
                getMob().forClan(channel -> channel.activateTask(ClanTaskKey.RUNITE_ORES, getMob().getName()));
            }

            if (getMob().prestige.hasPerk(PrestigePerk.THE_ROCK) && RandomUtils.success(.10)) {
                getMob().inventory.addOrDrop(new Item(harvest, 1));
            }

            int base_chance = ore.ordinal() * 45;
            int modified_chance = /*getMob().equipment.isWearingChargedGlory() ? (int) (base_chance / 2.2) :*/ base_chance;

            if (Utility.random(modified_chance) == 1) {
                if (getMob().inventory.getFreeSlots() != 0 && !gem) {
                    Item item = Mining.GEM_ITEMS.next();
                    getMob().inventory.add(item);
                    getMob().send(new SendMessage("You have found " + Utility.getAOrAn(item.getName()) + " " + item.getName() + "."));
                    AchievementHandler.activate(getMob(), AchievementKey.MINNING);
                }
            }

            if (object.active() && object.getGenericAttributes().get("ores", Integer.class) == -1 && ore.oreCount != -1) {
                this.cancel();
                getMob().resetAnimation();
                object.getGenericAttributes().set("ores", -1);
                getMob().skills.get(Skill.MINING).setDoingSkill(false);
                World.schedule(new ObjectReplacementEvent(object, ore.replacement, ore.respawn, () -> {
                    object.getGenericAttributes().set("ores", ore.oreCount);
                }));
            }
        }
        return true;
    }

    @Override
    protected boolean canSchedule() {
        return !getMob().skills.get(Skill.MINING).isDoingSkill();
    }

    @Override
    protected void onSchedule() {
        if (!object.getGenericAttributes().has("ores")) {
            object.getGenericAttributes().set("ores", ore.oreCount);
        }

        getMob().animate(pickaxe.animation);
    }

    @Override
    public void execute() {
        if (!getMob().skills.get(Skill.MINING).isDoingSkill()) {
            cancel();
            return;
        }
        if (object == null || !object.active() || object.getGenericAttributes() == null) {
            cancel();
            return;
        }

        if (!mine()) {
            cancel();
        }
    }

    @Override
    protected void onCancel(boolean logout) {
        getMob().resetFace();
        getMob().skills.get(Skill.MINING).setDoingSkill(false);
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public String getName() {
        return "mining-action";
    }
}