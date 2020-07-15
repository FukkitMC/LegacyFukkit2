package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.world.chunk.ServerChunkCache}
 */
public interface ServerChunkCacheExtra {

    net.minecraft.world.chunk.Chunk originalGetChunkAt(int var0, int var1);

    net.minecraft.world.chunk.Chunk getChunkAt(int var0, int var1, java.lang.Runnable var2);

    java.util.Collection a();

    net.minecraft.world.chunk.Chunk getChunkIfLoaded(int var0, int var1);
}
