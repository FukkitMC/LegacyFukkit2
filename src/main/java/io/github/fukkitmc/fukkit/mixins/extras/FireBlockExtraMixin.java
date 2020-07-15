package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.FireBlockExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link FireBlockExtra}
 */
@Mixin(net.minecraft.block.FireBlock.class)
public interface FireBlockExtraMixin extends FireBlockExtra {
}
