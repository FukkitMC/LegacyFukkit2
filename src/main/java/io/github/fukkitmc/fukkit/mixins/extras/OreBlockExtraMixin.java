package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.OreBlockExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link OreBlockExtra}
 */
@Mixin(net.minecraft.block.OreBlock.class)
public interface OreBlockExtraMixin extends OreBlockExtra {
}
