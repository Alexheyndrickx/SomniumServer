package com.osroyale.net.session;

import com.osroyale.Config;
import com.osroyale.game.world.World;
import com.osroyale.game.world.entity.mob.player.Player;
import com.osroyale.net.packet.GamePacket;
import com.osroyale.net.packet.PacketRepository;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Represents a {@link Session} when a {@link Player}
 * has been authenticated and active in the game world.
 *
 * @author nshusa
 */
public final class GameSession extends Session {

    private static final Logger logger = LogManager.getLogger();

    private final Queue<GamePacket> queuedPackets = new ConcurrentLinkedQueue<>();
    private final Queue<GamePacket> outgoingPackets = new ConcurrentLinkedQueue<>();

    private final Player player;

    GameSession(Channel channel, Player player) {
        super(channel);
        this.player = player;
    }

    @Override
    public void handleClientPacket(Object o) {
        if (o instanceof GamePacket) {
            queueClientPacket((GamePacket) o);
        }
    }

    @Override
    protected void onClose(ChannelFuture f) {
        World.queueLogout(player);
    }

    private void queueClientPacket(final GamePacket packet) {
        if (queuedPackets.size() <= Config.CLIENT_PACKET_THRESHOLD) {
            queuedPackets.offer(packet);
        }
    }

    public void processClientPackets() {
        for (int i = 0; i < Config.CLIENT_PACKET_THRESHOLD; i++) {
            try {
                GamePacket packet = queuedPackets.poll();

                if (packet == null) {
                    break;
                }

                PacketRepository.sendToListener(player, packet);
            } catch (Exception ex) {
                logger.error(String.format("error processing client packet for %s", player));
            }
        }
    }

    public void queueServerPacket(GamePacket packet) {
        outgoingPackets.offer(packet);
    }

    public void processServerPacketQueue() {
        for (int i = 0; i < Config.CLIENT_PACKET_THRESHOLD; i++) {
            GamePacket packet = outgoingPackets.poll();

            if (packet == null) {
                return;
            }

            channel.writeAndFlush(packet);
        }
    }

    public void flushPacket(GamePacket packet) {
        if (!channel.isOpen()) {
            return;
        }
        channel.writeAndFlush(packet);
    }

    public Player getPlayer() {
        return player;
    }

}
