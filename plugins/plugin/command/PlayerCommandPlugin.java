package plugin.command;

import com.osroyale.Config;
import com.osroyale.content.Yell;
import com.osroyale.content.achievement.AchievementHandler;
import com.osroyale.content.emote.EmoteHandler;
import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.content.teleport.TeleportHandler;
import com.osroyale.content.triviabot.TriviaBot;
import com.osroyale.game.plugin.extension.CommandExtension;
import com.osroyale.game.service.DonationService;
import com.osroyale.game.service.VoteService;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.UpdateFlag;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.command.Command;
import com.osroyale.game.world.entity.mob.player.command.CommandParser;
import com.osroyale.game.world.entity.skill.Skill;
import com.osroyale.game.world.position.Area;
import com.osroyale.game.world.position.Position;
import com.osroyale.net.packet.out.*;
import com.osroyale.util.MessageColor;
import com.osroyale.util.Utility;

import java.util.*;

public class PlayerCommandPlugin extends CommandExtension {

    @Override
    protected void register() {

        commands.add(new Command("commands", "command") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.send(new SendString("Commands List", 37103));
                player.send(new SendString("", 37107));
                for (int i = 0; i < 50; i++) {
                    player.send(new SendString("", i + 37111));
                }
                final Set<String> set = new HashSet<>();
                int count = 0;
                for (CommandExtension extension : extensions) {
                    if (!extension.canAccess(player)) {
                        continue;
                    }
                    final String clazzName = extension.getClass().getSimpleName().replace("CommandPlugin", "");
                    player.send(new SendString(clazzName + " Commands", count + 37111));
                    count++;
                    for (Map.Entry<String, Command> entry : extension.multimap.entries()) {
                        if (count >= 100) {
                            break;
                        }
                        if (set.contains(entry.getKey())) {
                            continue;
                        }
                        final Command command = entry.getValue();
                        final StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < command.getNames().length; i++) {
                            String name = command.getNames()[i];
                            builder.append("::");
                            builder.append(name);
                            if (i < command.getNames().length - 1) {
                                builder.append(", ");
                            }
                        }
                        player.send(new SendString(builder.toString(), count + 37111));
                        set.addAll(Arrays.asList(command.getNames()));
                        count++;
                    }
                }
                player.send(new SendScrollbar(37100, count * 22));
                player.interfaceManager.open(37100);

            }
        });

        commands.add(new Command("vote") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.send(new SendURL("https://www.runepk.io/vote"));
            }
        });

        commands.add(new Command("donate", "webstore") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.send(new SendURL("https://www.runepk.io/store"));
            }
        });

        commands.add(new Command("voted", "claimvote", "claimvotes") {
            @Override
            public void execute(Player player, CommandParser parser) {
                VoteService.claimReward(player);
            }
        });

        commands.add(new Command("donated") {
            @Override
            public void execute(Player player, CommandParser parser) {
                DonationService.claimDonation(player);
            }
        });

        commands.add(new Command("teleport") {
            @Override
            public void execute(Player player, CommandParser parser) {
                TeleportHandler.open(player);
            }
        });

        commands.add(new Command("staff", "staffonline", "staffon") {
            @Override
            public void execute(Player player, CommandParser parser) {
                List<Player> staffs = World.getStaff();
                int length = staffs.size() < 25 ? 25 : staffs.size();

                player.send(new SendString("", 37113));
                player.send(new SendString("", 37107));
                player.send(new SendString("RunePK Online Staff", 37103));
                player.send(new SendScrollbar(37110, length * 20));

                for (int index = 0, string = 37111; index < length; index++, string++) {
                    if (index < staffs.size()) {
                        Player staff = staffs.get(index);
                        player.send(new SendString(PlayerRight.getCrown(staff) + " " + staff.getName() + "    (<col=255>" + staff.right.getName() + "</col>)", string));
                    } else {
                        player.send(new SendString("", string));
                    }
                }

                player.send(new SendItemOnInterface(37199));
                player.interfaceManager.open(37100);
            }
        });

        commands.add(new Command("edgepvp", "pvp", "pvpinstance", "pvpmode") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (player.locking.locked())
                    return;
                if (PlayerRight.isIronman(player)) {
                    player.message("You can not do this as an ironman,");
                    return;
                }
                if (player.getCombat().isUnderAttackByPlayer()) {
                    player.message("You can not teleport whilst in combat!");
                    return;
                }
                if (player.pvpInstance) {
                    player.message("You are already in a PvP instance!");
                    return;
                }
                Teleportation.teleport(player, new Position(3093, 3494, 8), 20, () -> {
                    player.pvpInstance = true;
                    player.instance = 999999;
                    player.dialogueFactory.sendStatement("You have entered the PvP Instance!", "Players may attack each other outside of this bank.", "Teleport back home if you would like out.", "Good luck!").execute();
                    player.onStep();
                    if (player.pet != null) {
                        player.pet.instance = player.instance;
                    }
                });
            }
        });

        commands.add(new Command("home") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (player.getCombat().isUnderAttackByPlayer()) {
                    player.message("You can not teleport whilst in combat!");
                    return;
                }

                Teleportation.teleport(player, Config.DEFAULT_POSITION);
            }
        });

        commands.add(new Command("kdr") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.speak("My Kill Death Ratio: " + player.playerAssistant.kdr());
            }
        });

        commands.add(new Command("pure") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (PlayerRight.isIronman(player)) {
                    player.message("You can not do this as an ironman,");
                    return;
                }
                if (player.getCombat().inCombat()) {
                    player.message("You can not do this while in combat!");
                    return;
                }
                if (Area.inWilderness(player)) {
                    player.message("You can not do this while in the wilderness!");
                    return;
                }
                if (player.pvpInstance && Area.inPvP(player)) {
                    player.message("You must be in a safe zone to do this!");
                    return;
                }
                if (!player.equipment.isEmpty()) {
                    player.message("You can't be wearing anything to use this command!");
                    return;
                }
                player.skills.setMaxLevel(Skill.ATTACK, 60);
                player.skills.setMaxLevel(Skill.STRENGTH, 99);
                player.skills.setMaxLevel(Skill.DEFENCE, 1);
                player.skills.setMaxLevel(Skill.HITPOINTS, 99);
                player.skills.setMaxLevel(Skill.PRAYER, 52);
                player.skills.setMaxLevel(Skill.RANGED, 99);
                player.skills.setMaxLevel(Skill.MAGIC, 99);
                player.skills.setCombatLevel();
                player.updateFlags.add(UpdateFlag.APPEARANCE);
            }
        });

        commands.add(new Command("zerker") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (PlayerRight.isIronman(player)) {
                    player.message("You can not do this as an ironman,");
                    return;
                }
                if (player.getCombat().inCombat()) {
                    player.message("You can not do this while in combat!");
                    return;
                }
                if (Area.inWilderness(player)) {
                    player.message("You can not do this while in the wilderness!");
                    return;
                }
                if (player.pvpInstance && Area.inPvP(player)) {
                    player.message("You must be in a safe zone to do this!");
                    return;
                }
                if (!player.equipment.isEmpty()) {
                    player.message("You can't be wearing anything to use this command!");
                    return;
                }
                player.skills.setMaxLevel(Skill.ATTACK, 60);
                player.skills.setMaxLevel(Skill.STRENGTH, 99);
                player.skills.setMaxLevel(Skill.DEFENCE, 45);
                player.skills.setMaxLevel(Skill.HITPOINTS, 99);
                player.skills.setMaxLevel(Skill.PRAYER, 52);
                player.skills.setMaxLevel(Skill.RANGED, 99);
                player.skills.setMaxLevel(Skill.MAGIC, 99);
                player.skills.setCombatLevel();
                player.updateFlags.add(UpdateFlag.APPEARANCE);
            }
        });

        commands.add(new Command("master") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (PlayerRight.isIronman(player)) {
                    player.message("You can not do this as an ironman,");
                    return;
                }
                if (player.getCombat().inCombat()) {
                    player.message("You can not do this while in combat!");
                    return;
                }

                if (Area.inWilderness(player)) {
                    player.message("You can not do this while in the wilderness!");
                    return;
                }

                if (player.pvpInstance && Area.inPvP(player)) {
                    player.message("You must be in a safe zone to do this!");
                    return;
                }
                int count = PlayerRight.isAdministrator(player) ? Skill.SKILL_COUNT : 7;

                if (PlayerRight.isAdministrator(player)) {
                    player.skills.master();
                    AchievementHandler.completeAll(player);
                    EmoteHandler.unlockAll(player);
                    player.send(new SendMessage("Your account is now maxed out.", MessageColor.RED));
                    return;
                }

                for (int index = 0; index < count; index++) {
                    player.skills.setMaxLevel(index, 99);
                }

                player.skills.setCombatLevel();
                player.updateFlags.add(UpdateFlag.APPEARANCE);
            }
        });

        commands.add(new Command("lvl", "setlvl", "setskill", "setlevel") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (PlayerRight.isIronman(player)) {
                    player.message("You can not do this as an ironman,");
                    return;
                }
                if (player.getCombat().inCombat()) {
                    player.message("You can not do this while in combat!");
                    return;
                }

                if (Area.inWilderness(player)) {
                    player.message("You can not do this while in the wilderness!");
                    return;
                }

                if (player.pvpInstance && Area.inPvP(player)) {
                    player.message("You must be in a safe zone to do this!");
                    return;
                }

                int skill = parser.nextInt();
                int level = parser.nextInt();
                int count = PlayerRight.isAdministrator(player) ? Skill.SKILL_COUNT : 7;

                if (skill <= count) {
                    player.skills.setMaxLevel(skill, level);
                    player.skills.setCombatLevel();
                    player.send(new SendMessage("Your " + Skill.getName(skill) + " level has been set to " + level + "."));
                }
            }
        });

        commands.add(new Command("resetskills", "resetskill", "reset") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (PlayerRight.isIronman(player)) {
                    player.message("You can not do this as an ironman,");
                    return;
                }
                if (player.getCombat().inCombat()) {
                    player.message("You can not do this while in combat!");
                    return;
                }
                if (Area.inWilderness(player)) {
                    player.message("You can not do this while in the wilderness!");
                    return;
                }
                if (player.pvpInstance && Area.inPvP(player)) {
                    player.message("You must be in a safe zone to do this!");
                    return;
                }

                int count = PlayerRight.isAdministrator(player) ? Skill.SKILL_COUNT : 7;

                for (int index = 0; index < count; index++) {
                    player.skills.setLevel(index, index == 3 ? 10 : 1);
                    player.skills.setMaxLevel(index, index == 3 ? 10 : 1);
                }

                player.skills.setCombatLevel();
                player.updateFlags.add(UpdateFlag.APPEARANCE);
                player.send(new SendMessage("You have reset all your skills."));
            }
        });

        commands.add(new Command("players") {
            @Override
            public void execute(Player player, CommandParser parser) {
                player.send(new SendMessage("There are currently " + World.getPlayerCount() + " players playing RunePK!", MessageColor.RED));
            }
        });

        commands.add(new Command("empty", "emptyinventory", "clearinventory") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (Area.inWilderness(player) || player.getCombat().inCombat() || Area.inDuelArena(player) || player.playerAssistant.busy()) {
                    player.message("You can not clear your inventory at this current moment.");
                    return;
                }

                if (player.pvpInstance && Area.inPvP(player)) {
                    player.message("You must be in a safe zone to do this!");
                    return;
                }


                if (player.inventory.isEmpty()) {
                    player.message("You have nothing to empty!");
                    return;
                }

                String networth = Utility.formatDigits(player.playerAssistant.networth(player.inventory));
                player.dialogueFactory.sendStatement("Are you sure you want to clear your inventory? ",
                        "Container worth: <col=255>" + networth + " </col>coins.");
                player.dialogueFactory.sendOption("Yes", () -> {
                    player.inventory.clear(true);
                    player.dialogueFactory.clear();
                }, "Nevermind", () -> player.dialogueFactory.clear());
                player.dialogueFactory.execute();
            }
        });

        commands.add(new Command("yell") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    try {
                        StringBuilder message = new StringBuilder(parser.nextString());
                        while (parser.hasNext()) {
                            message.append(" ").append(parser.nextString());
                        }
                        Yell.yell(player, message.toString().trim());
                    } catch (final Exception e) {
                        player.send(new SendMessage("Invalid yell format, syntax: -messsage"));
                    }
                }
            }
        });

        commands.add(new Command("ans", "answer", "answers") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder answer = new StringBuilder();
                    while (parser.hasNext()) {
                        answer.append(parser.nextString()).append(" ");
                    }
                    TriviaBot.answer(player, answer.toString().trim());
                }
            }
        });
    }

    @Override
    public boolean canAccess(Player player) {
        return true;
    }

}
