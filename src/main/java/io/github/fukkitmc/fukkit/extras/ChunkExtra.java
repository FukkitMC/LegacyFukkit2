package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.world.chunk.Chunk}
 */
public interface ChunkExtra {

    void setNeighborUnloaded(int var0, int var1);

    void setNeighborLoaded(int var0, int var1);

    boolean areNeighborsLoaded(int var0);
}
