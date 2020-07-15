package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.WorldBorderExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link WorldBorderExtra}
 */
@Mixin(net.minecraft.world.border.WorldBorder.class)
public interface WorldBorderExtraMixin extends WorldBorderExtra {
}
