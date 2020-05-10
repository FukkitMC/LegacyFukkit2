package io.github.fukkitmc.legacy.extra;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.generator.ChunkGenerator;

public interface WorldExtra {

    CraftWorld getWorld();

    CraftServer getServer();

    void checkSleepStatus();

    void notifyAndUpdatePhysics(BlockPos blockposition, Chunk chunk, Block oldBlock, Block newBLock, int flag);

    boolean addEntity(Entity entity, CreatureSpawnEvent.SpawnReason spawnReason);

    void bukkitInit(ChunkGenerator gen, org.bukkit.World.Environment env);
}
