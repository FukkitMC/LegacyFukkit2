package org.bukkit.craftbukkit.chunkio;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ServerChunkCache;
import net.minecraft.world.chunk.ThreadedAnvilChunkStorage;

class QueuedChunk {
    final int x;
    final int z;
    final ThreadedAnvilChunkStorage loader;
    final World world;
    final ServerChunkCache provider;
    CompoundTag compound;

    public QueuedChunk(int x, int z, ThreadedAnvilChunkStorage loader, World world, ServerChunkCache provider) {
        this.x = x;
        this.z = z;
        this.loader = loader;
        this.world = world;
        this.provider = provider;
    }

    @Override
    public int hashCode() {
        return (x * 31 + z * 29) ^ world.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof QueuedChunk) {
            QueuedChunk other = (QueuedChunk) object;
            return x == other.x && z == other.z && world == other.world;
        }

        return false;
    }
}
