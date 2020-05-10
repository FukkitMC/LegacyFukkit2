package io.github.fukkitmc.legacy.extra;

import net.minecraft.server.*;
import net.minecraft.server.dedicated.AbstractPropertiesHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.UserCache;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.LevelInfo;

public interface MinecraftServerExtra {

    int getIdleTimeout();

    void setIdleTimeout(int timeout);

    AbstractPropertiesHandler getPropertyManager();

    String getVersion();

    boolean getSpawnAnimals();

    boolean getAllowFlight();

    boolean isHardcore();

    void safeShutdown();

    String getMotd();

    UserCache getUserCache();

    void stop();

    PlayerManager getPlayerList();

    boolean getOnlineMode();

    void setOnlineMode(boolean flag);

    void setPVP(boolean flag);

    boolean getPVP();

    void setSpawnAnimals(boolean flag);

    void setAllowFlight(boolean flag);

    void setMotd(String s);

    default boolean getAllowNether(){
        return true;
    }

    default LevelInfo.GameMode getGamemode(){
        return LevelInfo.GameMode.CREATIVE;
    }

    default boolean getGenerateStructures(){
        return true;
    }

    default Difficulty getDifficulty(){
        return Difficulty.EASY;
    }

    void a(Difficulty enumdifficulty);

    default boolean getSpawnMonsters(){
        return true;
    }

    boolean isRunning();

    String getResourcePack();

    String getResourcePackHash();

    ServerWorld getWorldServer(int i);

    String getServerIp();
}