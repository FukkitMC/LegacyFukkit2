package io.github.fukkitmc.legacy.mixins.extra;


import io.github.fukkitmc.legacy.extra.ChunkProviderServerExtra;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ServerChunkCache;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerChunkCache.class)
public class ChunkProviderServerMixin implements ChunkProviderServerExtra {
    @Override
    public Chunk originalGetChunkAt(int i, int j) {
        return null;
    }

    @Override
    public Chunk getChunkIfLoaded(int x, int z) {
        return null;
    }
}
