package com.osroyale.io;

import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketRepository;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * The class that loads all packet listeners.
 *
 * @author nshusa
 */
public final class PacketListenerLoader implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void run() {
        new FastClasspathScanner().matchClassesImplementing(PacketListener.class, subclass -> {
            PacketListenerMeta annotation = subclass.getAnnotation(PacketListenerMeta.class);
            try {
                PacketListener listener = subclass.newInstance();
                Arrays.stream(annotation.value()).forEach(it -> PacketRepository.registerListener(it, listener));
            } catch (InstantiationException | IllegalAccessException ex) {
                logger.error("error loading packet listeners.", ex);
            }
        }).scan();
    }

}
