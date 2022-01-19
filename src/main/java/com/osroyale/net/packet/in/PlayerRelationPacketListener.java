package com.osroyale.net.packet.in;

import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.relations.PrivateChatMessage;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.ClientPackets;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.util.ChatCodec;
import com.osroyale.util.Utility;

import java.util.Optional;

/**
 * The {@link GamePacket}'s responsible for player communication.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta({ClientPackets.ADD_FRIEND, ClientPackets.PRIVATE_MESSAGE, ClientPackets.REMOVE_FRIEND, ClientPackets.REMOVE_IGNORE, ClientPackets.ADD_IGNORE})
public final class PlayerRelationPacketListener implements PacketListener {

    @Override
    public void handlePacket(Player player, GamePacket packet) {
        final long username = packet.readLong();
        switch (packet.getOpcode()) {

            case ClientPackets.ADD_FRIEND:
                player.relations.addFriend(username);
                break;

            case ClientPackets.REMOVE_FRIEND:
                player.relations.deleteFriend(username);
                break;

            case ClientPackets.ADD_IGNORE:
                player.relations.addIgnore(username);
                break;

            case ClientPackets.REMOVE_IGNORE:
                player.relations.deleteIgnore(username);
                break;

            case ClientPackets.PRIVATE_MESSAGE:
                final Optional<Player> result = World.search(Utility.formatText(Utility.longToString(username)).replace("_", " "));

                if (!result.isPresent()) {
                    break;
                }

                final Player other = result.get();

                final byte[] input = packet.readBytes(packet.getSize() - Long.BYTES);
                final String decoded = ChatCodec.decode(input);
                final byte[] compressed = ChatCodec.encode(decoded);
                player.relations.message(other, new PrivateChatMessage(decoded, compressed));
                break;
        }

    }

}
