package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.ThreadedAnvilChunkStorageExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link ThreadedAnvilChunkStorageExtra}
 */
@Mixin(net.minecraft.world.chunk.ThreadedAnvilChunkStorage.class)
public interface ThreadedAnvilChunkStorageExtraMixin extends ThreadedAnvilChunkStorageExtra {
}
