package plugin.command;

import com.osroyale.Config;
import com.osroyale.content.dialogue.DialogueFactory;
import com.osroyale.content.skill.impl.magic.teleport.Teleportation;
import com.osroyale.game.plugin.extension.CommandExtension;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.AccountSecurity;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.game.world.entity.mob.player.PlayerRight;
import com.osroyale.game.world.entity.mob.player.command.Command;
import com.osroyale.game.world.entity.mob.player.command.CommandParser;
import com.osroyale.net.packet.out.SendInputAmount;
import com.osroyale.net.packet.out.SendMessage;
import com.osroyale.util.MessageColor;

import java.util.concurrent.TimeUnit;

public class HelperCommandPlugin extends CommandExtension {

    @Override
    protected void register() {
        commands.add(new Command("mute") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());

                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }

                    World.search(name.toString()).ifPresent(other -> {
                        if (PlayerRight.isAdministrator(other) && !PlayerRight.isDeveloper(player)) {
                            player.message("You do not have permission to mute this player!");
                            return;
                        }

                        DialogueFactory factory = player.dialogueFactory;
                        factory.sendOption("Mute by day", () -> {
                            factory.onAction(() -> player.send(new SendInputAmount("How long do you want this mute to last for?", 2, input -> {
                                other.punishment.mute(Integer.parseInt(input), TimeUnit.DAYS);
                                factory.clear();
                            })));
                        }, "Mute by hour", () -> {
                            factory.onAction(() -> player.send(new SendInputAmount("How long do you want this mute to last for?", 3, input -> {
                                other.punishment.mute(Integer.parseInt(input), TimeUnit.HOURS);
                                factory.clear();
                            })));
                        }, "Mute by minute", () -> {
                            factory.onAction(() -> player.send(new SendInputAmount("How long do you want this mute to last for?", 3, input -> {
                                other.punishment.mute(Integer.parseInt(input), TimeUnit.MINUTES);
                                factory.clear();
                            })));
                        }, "Mute forever", () -> {
                            factory.onAction(() -> {
                                other.punishment.mute(9999, TimeUnit.DAYS);
                                factory.clear();
                            });
                        }).execute();
                    });
                } else {
                    player.message("Invalid command use; ::mute daniel");
                }
            }
        });

        commands.add(new Command("unmute") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());

                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }

                    World.search(name.toString()).ifPresent(other -> {
                        other.punishment.unmute();
                        other.dialogueFactory.sendStatement("You have been unmuted!").execute();
                        player.message("unmute was complete");

                    });
                } else {
                    player.message("Invalid command use; ::unmute daniel");
                }
            }
        });

        commands.add(new Command("jail") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());

                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }

                    World.search(name.toString()).ifPresent(other -> {
                        if (PlayerRight.isAdministrator(other) && !PlayerRight.isDeveloper(player)) {
                            player.message("You do not have permission to jail this player!");
                            return;
                        }

                        DialogueFactory factory = player.dialogueFactory;
                        factory.sendOption("Jail by day", () -> {
                            factory.onAction(() -> player.send(new SendInputAmount("How long do you want this jail to last for?", 2, input -> {
                                other.punishment.jail(Integer.parseInt(input), TimeUnit.DAYS);
                                factory.clear();
                            })));
                        }, "Jail by hour", () -> {
                            factory.onAction(() -> player.send(new SendInputAmount("How long do you want this jail to last for?", 3, input -> {
                                other.punishment.jail(Integer.parseInt(input), TimeUnit.HOURS);
                                factory.clear();
                            })));
                        }, "Jail by minute", () -> {
                            factory.onAction(() -> player.send(new SendInputAmount("How long do you want this jail to last for?", 3, input -> {
                                other.punishment.jail(Integer.parseInt(input), TimeUnit.MINUTES);
                                factory.clear();
                            })));
                        }, "Jail forever", () -> {
                            factory.onAction(() -> {
                                other.punishment.jail(9999, TimeUnit.DAYS);
                                factory.clear();
                            });
                        }).execute();
                    });
                } else {
                    player.message("Invalid command use; ::jail daniel");
                }
            }
        });

        commands.add(new Command("unjail") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    StringBuilder name = new StringBuilder(parser.nextString());

                    while (parser.hasNext()) {
                        name.append(" ").append(parser.nextString());
                    }
                    World.search(name.toString()).ifPresent(other -> {
                        other.punishment.unJail();
                        other.dialogueFactory.sendStatement("You have been unjailed!").execute();
                        player.message("unjail was complete");
                    });

                } else {
                    player.message("Invalid command use; ::unjail daniel");
                }
            }
        });

        commands.add(new Command("staffzone", "sz") {
            @Override
            public void execute(Player player, CommandParser parser) {
                Teleportation.teleport(player, Config.STAFF_ZONE);
                player.send(new SendMessage("Welcome to the staffzone, " + player.getName() + "."));
            }
        });

        commands.add(new Command("key", "pin") {
            @Override
            public void execute(Player player, CommandParser parser) {
                if (parser.hasNext()) {
                    String key = parser.nextString();

                    if (!player.locking.locked()) {
                        player.send(new SendMessage("Your account is not locked, there is no need to enter a key."));
                        return;
                    }

                    if (AccountSecurity.AccountData.forName(player.getName()).isPresent()) {
                        AccountSecurity.AccountData account = AccountSecurity.AccountData.forName(player.getName()).get();

                        if (account.getKey().equalsIgnoreCase(key)) {
                            player.send(new SendMessage("You have entered the assigned security key for this account. Your session was", MessageColor.RED));
                            player.send(new SendMessage("activated.", MessageColor.RED));
                            player.move(Config.DEFAULT_POSITION);
                            player.locking.unlock();
                            player.setVisible(true);
                        } else {
                            player.send(new SendMessage("You have entered the wrong key!", MessageColor.RED));
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean canAccess(Player player) {
        return PlayerRight.isHelper(player);
    }

}
