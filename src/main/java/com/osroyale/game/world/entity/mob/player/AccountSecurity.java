package com.osroyale.game.world.entity.mob.player;

import com.osroyale.Config;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.data.LockType;
import com.osroyale.game.world.entity.mob.player.profile.Profile;
import com.osroyale.game.world.entity.mob.player.profile.ProfileRepository;

import java.util.Arrays;
import java.util.Optional;

/**
 * Handles account security.
 *
 * @author Daniel
 */
public class AccountSecurity {

    /** The player instance. */
    private Player player;

    /** Constructs a new <code>AccountSecurity<code>. */
    AccountSecurity(Player player) {
        this.player = player;
    }

    /** Handles account login. */
    public void login() {
        String name = player.getName();
        String host = player.lastHost;

        if (!player.hostList.contains(host))
            player.hostList.add(host);

        ProfileRepository.put(new Profile(name, player.getPassword(), host, player.hostList, player.right));

        if (!AccountData.forName(name).isPresent()) {
            if (player.right == PlayerRight.OWNER || player.right == PlayerRight.ADMINISTRATOR || player.right == PlayerRight.DEVELOPER) {
                player.right = PlayerRight.PLAYER;
                player.inventory.clear();
                player.equipment.clear();
                player.votePoints = 0;
                player.donation.setCredits(0);
                player.pestPoints = 0;
                player.mageArenaPoints = 0;
                player.skillingPoints = 0;
                player.bank.clear();
                player.setVisible(true);
            } else if (PlayerRight.isDonator(player)) {
                player.setVisible(true);
                player.donation.updateRank(true);
            }
            return;
        }

        player.interfaceManager.close();
        AccountData account = AccountData.forName(name).get();
        player.setVisible(true);

        if (Config.SERVER_DEBUG || host.equals("127.0.0.1")) {
            return;
        }

        if (account.getName().equalsIgnoreCase(name)) {
            for (String hosts : account.getHost()) {
                if (host.equalsIgnoreCase(hosts))
                    return;
            }

            if (account.getKey().isEmpty()) {
                return;
            }

            player.locking.lock(LockType.MASTER_WITH_COMMANDS);
            player.message("<col=F03541>You have logged in with an un-authorized IP address. Your account was locked. Please");
            player.message("<col=F03541>enter your security key by command. ::key 12345");
            World.sendStaffMessage("<col=E02828>[AccountSecurity] Un-recognized staff host address : " + player.getName() + ".");
        }
    }

    /** Holds all the account security data for the management team. */
    public enum AccountData {
        /* Owner */
        DANIEL(PlayerRight.OWNER, "Daniel", "12", "127.0.0.1"),
        ALEX(PlayerRight.OWNER, "Alex", "12", "127.0.0.1"),

        ;

        private final String name;
        private final String key;
        private final PlayerRight right;
        private final String[] host;

        AccountData(PlayerRight right, String name, String key, String... host) {
            this.right = right;
            this.name = name;
            this.key = key;
            this.host = host;
        }


        public static Optional<AccountData> forName(String name) {
            for (AccountData account : values()) {
                if (account.getName().equalsIgnoreCase(name)) {
                    return Optional.of(account);
                }
            }
            return Optional.empty();
        }

        public String getName() {
            return name;
        }

        public PlayerRight getRight() {
            return right;
        }

        public String getKey() {
            return key;
        }

        public String[] getHost() {
            return host;
        }
    }
}
