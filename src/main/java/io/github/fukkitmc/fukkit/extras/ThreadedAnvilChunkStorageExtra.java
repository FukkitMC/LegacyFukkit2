package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.world.chunk.ThreadedAnvilChunkStorage}
 */
public interface ThreadedAnvilChunkStorageExtra {

    java.lang.Object[] a(net.minecraft.world.World var0, int var1, int var2, net.minecraft.nbt.CompoundTag var3);

    boolean chunkExists(net.minecraft.world.World var0, int var1, int var2);

    java.lang.Object[] loadChunk(net.minecraft.world.World var0, int var1, int var2);

    void loadEntities(net.minecraft.world.chunk.Chunk var0, net.minecraft.nbt.CompoundTag var1, net.minecraft.world.World var2);
}
