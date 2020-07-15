package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.world.World}
 */
public interface WorldExtra {

    org.bukkit.craftbukkit.CraftWorld getCraftWorld();

    org.bukkit.craftbukkit.CraftServer getServer();

    net.minecraft.world.chunk.Chunk getChunkIfLoaded(int var0, int var1);

    boolean addEntity(net.minecraft.entity.Entity var0, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason var1);

    void notifyAndUpdatePhysics(net.minecraft.util.math.BlockPos var0, net.minecraft.world.chunk.Chunk var1, net.minecraft.block.Block var2, net.minecraft.block.Block var3, int var4);

    void checkSleepStatus();
}
