package com.osroyale.game.service;

import com.osroyale.OSRoyale;
import com.osroyale.net.ServerPipelineInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * The bootstrap that will prepare the game and net.
 * @author Seven
 */
public final class NetworkService {

	private static final Logger logger = LogManager.getLogger();

	public void start(int port) throws Exception {
		logger.info("Starting network service on port: " + port);

		ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
		final EventLoopGroup bossGroup = new NioEventLoopGroup();
		final EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ServerPipelineInitializer());

			ChannelFuture f = b.bind(port).syncUninterruptibly();

			OSRoyale.serverStarted.set(true);

			logger.info(String.format("Server built successfully (took %d seconds).", OSRoyale.UPTIME.elapsedTime(TimeUnit.SECONDS)));
			OSRoyale.UPTIME.reset();
			f.channel().closeFuture().sync();
		} catch (Exception ex) {
			logger.error("error starting network service.", ex);
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}