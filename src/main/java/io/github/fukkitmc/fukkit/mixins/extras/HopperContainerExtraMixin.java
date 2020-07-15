package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.HopperContainerExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link HopperContainerExtra}
 */
@Mixin(net.minecraft.container.HopperContainer.class)
public interface HopperContainerExtraMixin extends HopperContainerExtra {
}
