package org.bukkit.craftbukkit.util;

import java.util.HashSet;
import java.util.List;

import io.github.fukkitmc.legacy.extra.EntityExtra;
import io.github.fukkitmc.legacy.extra.MinecraftServerExtra;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;

import org.bukkit.entity.Player;

public class LazyPlayerSet extends LazyHashSet<Player> {

    @Override
    HashSet<Player> makeReference() {
        if (reference != null) {
            throw new IllegalStateException("Reference already created!");
        }
        List<ServerPlayerEntity> players = ((MinecraftServerExtra)MinecraftServer.getServer()).getPlayerList().players;
        HashSet<Player> reference = new HashSet<Player>(players.size());
        for (ServerPlayerEntity player : players) {
            reference.add((Player) ((EntityExtra)player).getBukkitEntity());
        }
        return reference;
    }

}
