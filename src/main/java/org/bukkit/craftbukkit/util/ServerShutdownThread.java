package org.bukkit.craftbukkit.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.WorldLoadException;

public class ServerShutdownThread extends Thread {
    private final MinecraftServer server;

    public ServerShutdownThread(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            server.stop();
        } catch (WorldLoadException ex) {
            ex.printStackTrace();
        } finally {
            try {
                server.reader.getTerminal().restore();
            } catch (Exception e) {
            }
        }
    }
}
