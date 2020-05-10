package org.bukkit.craftbukkit.chunkio;

import java.io.IOException;

import io.github.fukkitmc.legacy.extra.ChunkExtra;
import io.github.fukkitmc.legacy.extra.ChunkProviderServerExtra;
import io.github.fukkitmc.legacy.extra.ChunkRegionLoaderExtra;
import io.github.fukkitmc.legacy.extra.WorldExtra;
import org.bukkit.Server;
import org.bukkit.craftbukkit.util.AsynchronousExecutor;
import org.bukkit.craftbukkit.util.LongHash;

import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ThreadedAnvilChunkStorage;

class ChunkIOProvider implements AsynchronousExecutor.CallBackProvider<QueuedChunk, Chunk, Runnable, RuntimeException> {
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    // async stuff
    public Chunk callStage1(QueuedChunk queuedChunk) throws RuntimeException {
        try {
            ThreadedAnvilChunkStorage loader = queuedChunk.loader;
            Object[] data = ((ChunkRegionLoaderExtra)loader).loadChunk(queuedChunk.world, queuedChunk.x, queuedChunk.z);
            
            if (data != null) {
                queuedChunk.compound = (CompoundTag) data[1];
                return (Chunk) data[0];
            }

            return null;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    // sync stuff
    public void callStage2(QueuedChunk queuedChunk, Chunk chunk) throws RuntimeException {
        if (chunk == null) {
            // If the chunk loading failed just do it synchronously (may generate)
            ((ChunkProviderServerExtra)queuedChunk.provider).originalGetChunkAt(queuedChunk.x, queuedChunk.z);
            return;
        }

        ((ChunkRegionLoaderExtra)queuedChunk.loader).loadEntities(chunk, queuedChunk.compound.getCompound("Level"), queuedChunk.world);
        chunk.setLastSaved(queuedChunk.provider.world.getTime());
        queuedChunk.provider.chunks.set(LongHash.toLong(queuedChunk.x, queuedChunk.z), chunk);
        chunk.addEntities();

        if (queuedChunk.provider.chunkProvider != null) {
            queuedChunk.provider.chunkProvider.recreateStructures(chunk, queuedChunk.x, queuedChunk.z);
        }

        Server server = ((WorldExtra)queuedChunk.provider.world).getServer();
        if (server != null) {
            server.getPluginManager().callEvent(new org.bukkit.event.world.ChunkLoadEvent(chunk.bukkitChunk, false));
        }

        // Update neighbor counts
        for (int x = -2; x < 3; x++) {
            for (int z = -2; z < 3; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }

                Chunk neighbor = ((ChunkProviderServerExtra)queuedChunk.provider).getChunkIfLoaded(chunk.locX + x, chunk.locZ + z);
                if (neighbor != null) {
                    ((ChunkExtra)neighbor).setNeighborLoaded(-x, -z);
                    ((ChunkExtra)chunk).setNeighborLoaded(x, z);
                }
            }
        }

        chunk.loadNearby(queuedChunk.provider, queuedChunk.provider, queuedChunk.x, queuedChunk.z);
    }

    public void callStage3(QueuedChunk queuedChunk, Chunk chunk, Runnable runnable) throws RuntimeException {
        runnable.run();
    }

    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, "Chunk I/O Executor Thread-" + threadNumber.getAndIncrement());
        thread.setDaemon(true);
        return thread;
    }
}
