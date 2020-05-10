package io.github.fukkitmc.legacy.redirects;

import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;

public class MinecraftServerRedirects {

    public static MinecraftServer getServer(){
        return ((CraftServer) Bukkit.getServer()).getServer();
    }
    public static final Logger LOGGER = MinecraftServer.LOGGER;


}
