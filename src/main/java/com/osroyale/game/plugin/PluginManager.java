package com.osroyale.game.plugin;

import com.osroyale.game.event.bus.PlayerDataBus;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * This class handles how plugins are loaded/unloaded and accessed.
 *
 * @author nshusa
 */
public final class PluginManager {
    private static final Logger logger = LogManager.getLogger();
    private static final Set<String> plugins = new HashSet<>();
    private static final PlayerDataBus dataBus = PlayerDataBus.getInstance();

    private PluginManager() {

    }

    public static void load() {
        new FastClasspathScanner().matchSubclassesOf(PluginContext.class, clazz -> {
            try {
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    PluginContext listener = clazz.getConstructor().newInstance();
                    listener.onInit();
                    dataBus.subscribe(listener);
                    plugins.add(clazz.getName());
                }
            } catch (Exception ex) {
                logger.error(String.format("Error loading plugin=%s", clazz.getSimpleName()), ex);
            }
        }).scan();
        logger.info(String.format("Loaded: %d plugins.", PluginManager.getPluginCount()));
    }

    public static int getPluginCount() {
        return plugins.size();
    }

    public static PlayerDataBus getDataBus() {
        return dataBus;
    }

}
