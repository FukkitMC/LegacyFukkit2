package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.RedstoneOreBlockExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link RedstoneOreBlockExtra}
 */
@Mixin(net.minecraft.block.RedstoneOreBlock.class)
public interface RedstoneOreBlockExtraMixin extends RedstoneOreBlockExtra {
}
