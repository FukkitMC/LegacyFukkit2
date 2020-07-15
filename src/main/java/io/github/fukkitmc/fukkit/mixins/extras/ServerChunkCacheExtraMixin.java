package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.ServerChunkCacheExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link ServerChunkCacheExtra}
 */
@Mixin(net.minecraft.world.chunk.ServerChunkCache.class)
public interface ServerChunkCacheExtraMixin extends ServerChunkCacheExtra {
}
