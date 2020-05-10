package io.github.fukkitmc.legacy.mixins.extra;

import io.github.fukkitmc.legacy.extra.MinecraftServerExtra;
import net.minecraft.server.*;
import net.minecraft.server.dedicated.AbstractPropertiesHandler;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.UserCache;
import net.minecraft.world.Difficulty;
import org.bukkit.craftbukkit.CraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.FutureTask;

@Mixin(value = MinecraftServer.class, remap = false)
public class MinecraftServerMixin implements MinecraftServerExtra {

    @Shadow public CraftServer server;

    @Override
    public int getIdleTimeout() {
        return ((MinecraftServer)(Object)this).playerIdleTimeout;
    }

    @Override
    public void setIdleTimeout(int timeout) {
        ((MinecraftServer)(Object)this).playerIdleTimeout = timeout;
    }

    @Override
    public AbstractPropertiesHandler getPropertyManager() {
        return ((MinecraftDedicatedServer) (Object) this).propertyManager;
    }

    @Override
    public String getVersion() {
        return "1.8.9";
    }

    @Override
    public boolean getSpawnAnimals() {
        return true;
    }

    @Override
    public void setSpawnAnimals(boolean flag) {

    }

    @Override
    public boolean getAllowFlight() {
        return true;
    }

    @Override
    public void setAllowFlight(boolean flag) {

    }

    @Override
    public boolean isHardcore() {
        return false;
    }

    @Override
    public void safeShutdown() {
//        this.isRunning = false;
    }

    @Override
    public String getMotd() {
        return ((MinecraftServer)(Object)this).motd;
    }

    @Override
    public void setMotd(String s) {

    }

    @Override
    public void a(Difficulty enumdifficulty) {

    }

    @Override
    public UserCache getUserCache() {
        return ((MinecraftServer)(Object)this).Z;
    }

    @Override
    public void stop() {

    }

    @Override
    public PlayerManager getPlayerList() {
        return ((MinecraftServer)(Object)this).playerManager;
    }

    @Override
    public boolean getOnlineMode() {
        return false;
    }

    @Override
    public void setOnlineMode(boolean flag) {

    }

    @Override
    public boolean getPVP() {
        return false;
    }

    @Override
    public void setPVP(boolean flag) {

    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public String getResourcePack() {
        return ((MinecraftServer)(Object)this).resourcePackUrl;
    }

    @Override
    public String getResourcePackHash() {
        return ((MinecraftServer)(Object)this).resourcePackHash;
    }

    @Override
    public ServerWorld getWorldServer(int i) {
        // CraftBukkit start
        for (ServerWorld world : ((MinecraftServer)(Object)this).worlds) {
            if (world.dimension == i) {
                return world;
            }
        }
        return ((MinecraftServer)(Object)this).worlds.get(0);
        // CraftBukkit end
    }

    @Override
    public String getServerIp() {
        return this.server.getIp();
    }
}
