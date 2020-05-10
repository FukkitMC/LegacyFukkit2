package io.github.fukkitmc.legacy.extra;

import java.io.IOException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public interface ChunkRegionLoaderExtra {

    Object[] loadChunk(World world, int i, int j) throws IOException;

    void loadEntities(Chunk chunk, CompoundTag nbttagcompound, World world);


}
