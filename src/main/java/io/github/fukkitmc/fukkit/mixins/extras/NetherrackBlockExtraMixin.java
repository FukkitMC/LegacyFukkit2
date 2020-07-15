package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.NetherrackBlockExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link NetherrackBlockExtra}
 */
@Mixin(net.minecraft.block.NetherrackBlock.class)
public interface NetherrackBlockExtraMixin extends NetherrackBlockExtra {
}
