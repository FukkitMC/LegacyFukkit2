package org.bukkit.craftbukkit.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.storage.WorldLoadException;

public class ServerShutdownThread extends Thread {
    private final DedicatedServer server;

    public ServerShutdownThread(DedicatedServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            server.stopRunning();
        } finally {
            try {
//                server.reader.getTerminal().restore();
            } catch (Exception e) {
            }
        }
    }
}
