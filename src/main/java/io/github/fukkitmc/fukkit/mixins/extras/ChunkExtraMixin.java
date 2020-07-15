package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.ChunkExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link ChunkExtra}
 */
@Mixin(net.minecraft.world.chunk.Chunk.class)
public interface ChunkExtraMixin extends ChunkExtra {
}
