package io.github.fukkitmc.legacy.extra;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.bukkit.Location;

public interface PlayerListExtra {

    ServerPlayerEntity moveToWorld(ServerPlayerEntity entityplayer, int i, boolean flag, Location location, boolean avoidSuffocation);


    void setPlayerFileData(ServerWorld[] aworldserver);

    void onPlayerJoin(ServerPlayerEntity player, String joinMessage);
}
