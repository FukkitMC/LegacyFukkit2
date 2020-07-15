package org.bukkit.craftbukkit.chunkio;

import java.io.IOException;
import org.bukkit.Server;
import org.bukkit.craftbukkit.util.AsynchronousExecutor;
import org.bukkit.craftbukkit.util.LongHash;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ThreadedAnvilChunkStorage;

class ChunkIOProvider implements AsynchronousExecutor.CallBackProvider<QueuedChunk, Chunk, Runnable, RuntimeException> {
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    // async stuff
    public Chunk callStage1(QueuedChunk queuedChunk) throws RuntimeException {
        try {
            ThreadedAnvilChunkStorage loader = queuedChunk.loader;
            Object[] data = loader.loadChunk(queuedChunk.world, queuedChunk.x, queuedChunk.z);
            
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
            queuedChunk.provider.originalGetChunkAt(queuedChunk.x, queuedChunk.z);
            return;
        }

        queuedChunk.loader.loadEntities(chunk, queuedChunk.compound.getCompound("Level"), queuedChunk.world);
        chunk.method_1370(queuedChunk.provider.world.getTime());
        queuedChunk.provider.chunks.put(LongHash.toLong(queuedChunk.x, queuedChunk.z), chunk);
        chunk.method_1374();

        if (queuedChunk.provider.field_6615 != null) {
            queuedChunk.provider.world.timings.syncChunkLoadStructuresTimer.startTiming(); // Spigot
            queuedChunk.provider.field_6615.method_1325(chunk, queuedChunk.x, queuedChunk.z);
            queuedChunk.provider.world.timings.syncChunkLoadStructuresTimer.stopTiming(); // Spigot
        }

        Server server = queuedChunk.provider.world.getServer();
        if (server != null) {
            server.getPluginManager().callEvent(new org.bukkit.event.world.ChunkLoadEvent(chunk.bukkitChunk, false));
        }

        // Update neighbor counts
        for (int x = -2; x < 3; x++) {
            for (int z = -2; z < 3; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }

                Chunk neighbor = queuedChunk.provider.getChunkIfLoaded(chunk.field_1562 + x, chunk.field_1563 + z);
                if (neighbor != null) {
                    neighbor.setNeighborLoaded(-x, -z);
                    chunk.setNeighborLoaded(x, z);
                }
            }
        }

        chunk.method_1351(queuedChunk.provider, queuedChunk.provider, queuedChunk.x, queuedChunk.z);
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
