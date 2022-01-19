package com.osroyale.fs.cache.decoder;

import com.osroyale.fs.cache.FileSystem;
import com.osroyale.fs.cache.archive.Archive;
import com.osroyale.fs.util.ByteBufferUtil;
import com.osroyale.game.world.object.GameObjectDefinition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;

/**
 * A class which parses object definitions.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class ObjectDefinitionDecoder implements Runnable {

    /** The logger to log process output. */
    private final static Logger LOGGER = LogManager.getLogger();

    /** The IndexedFileSystem. */
    private final FileSystem fs;

    /**
     * Creates the {@link ObjectDefinitionDecoder}.
     *
     * @param fs The {@link FileSystem}.
     */
    public ObjectDefinitionDecoder(FileSystem fs) {
        this.fs = fs;
    }

    @Override
    public void run() {
        LOGGER.info("Loading object definitions.");
        Archive archive = fs.getArchive(FileSystem.CONFIG_ARCHIVE);
        ByteBuffer dat = archive.getData("loc.dat");
        ByteBuffer idx = archive.getData("loc.idx");

        int count = idx.getShort() & 0xFFFF;

        if (count != GameObjectDefinition.MAX_DEFINITIONS) {
            throw new AssertionError("GameObjectDefinition size should be " + count + ", not " + GameObjectDefinition.MAX_DEFINITIONS);
        }

        int pos = 2;
        int loaded = 0;
        for (int index = 0; index < count; index++) {
            dat.position(pos);
            decode(index, dat);
            pos += idx.getShort() & 0xFFFF;
            loaded++;
        }
        LOGGER.info("Loaded " + loaded + " object definitions.");
    }

    /**
     * Parses a single game object definition by reading object info from a
     * buffer.
     *
     * @param id  The id of the object.
     * @param buf The buffer.
     */
    private static void decode(int id, ByteBuffer buf) {
        String name = "null";
        String desc = "null";
        int width = 1;
        int length = 1;
        boolean solid = true;
        boolean impenetrable = true;
        boolean hasActions = false;
        boolean interactive = false;
        boolean wall = false;
        boolean decoration = false;
        boolean removeClipping = false;
        boolean models = false;
        boolean is10 = true;

        do {
            int opcode = buf.get();
            if (opcode == 0) break;

            if (opcode == 1) {
                int len = buf.get();
                if (len > 0 && !models) {
                    for (int idx = 0; idx < len; idx++) {
                        buf.position(buf.position() + Short.BYTES);
                        if (buf.get() != 10 && idx == 0)
                            is10 = false;
                    }
                } else {
                    buf.position(buf.position() + len * (Byte.BYTES + Short.BYTES));
                }
            } else if (opcode == 2) {
                name = ByteBufferUtil.getJString(buf);
            } else if (opcode == 3) {
                desc = ByteBufferUtil.getJString(buf);
            } else if (opcode == 5) {
                int len = buf.get();
                if (len > 0) {
                    models = true;
                    buf.position(buf.position() + len * Short.BYTES);
                }
            } else if (opcode == 14) {
                width = buf.get();
            } else if (opcode == 15) {
                length = buf.get();
            } else if (opcode == 17) {
                solid = false;
            } else if (opcode == 18) {
                impenetrable = false;
            } else if (opcode == 19) {
                hasActions = (buf.get() == 1);
            } else if (opcode == 24) {
                buf.getShort();
            } else if (opcode == 28) {
                buf.get();
            } else if (opcode == 29) {
                buf.get();
            } else if (opcode == 39) {
                buf.get();
            } else if (opcode >= 30 && opcode < 39) {
                ByteBufferUtil.getJString(buf);
                interactive = true;
            } else if (opcode == 40) {
                int len = buf.get();
                buf.position(buf.position() + 2 * len * Short.BYTES);
            } else if (opcode == 41) {
                int len = buf.get();
                buf.position(buf.position() + 2 * len * Short.BYTES);
            } else if (opcode == 82) {
                buf.getShort();
            } else if (opcode == 62) {
                wall = true;
            } else if (opcode == 65) {
                buf.getShort();
            } else if (opcode == 66) {
                buf.getShort();
            } else if (opcode == 67) {
                buf.getShort();
            } else if (opcode == 68) {
                buf.getShort();
            } else if (opcode == 69) {
                buf.get();
            } else if (opcode == 70) {
                buf.getShort();
            } else if (opcode == 71) {
                buf.getShort();
            } else if (opcode == 72) {
                buf.getShort();
            } else if (opcode == 73) {
                decoration = true;
            } else if (opcode == 74) {
                removeClipping = true;
            } else if (opcode == 75) {
                buf.get();
            } else if (opcode == 77) {
                buf.position(buf.position() + 2 * Short.BYTES);
                int len = buf.get();
                buf.position(buf.position() + (len + 1) * Short.BYTES);
            }
        } while (true);

        if (name != null && !name.equals("null")) {
            hasActions = models && is10;
            if (interactive)
                hasActions = true;
        }

        if (removeClipping) {
            solid = false;
            impenetrable = false;
        }

        GameObjectDefinition.addDefinition(new GameObjectDefinition(id, name, desc, width, length, solid, impenetrable, hasActions, wall, decoration));
    }
}
