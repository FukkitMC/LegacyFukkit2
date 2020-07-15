package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.BlockExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link BlockExtra}
 */
@Mixin(net.minecraft.block.Block.class)
public interface BlockExtraMixin extends BlockExtra {
}
