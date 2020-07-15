package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.BrewingStandContainerExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link BrewingStandContainerExtra}
 */
@Mixin(net.minecraft.container.BrewingStandContainer.class)
public interface BrewingStandContainerExtraMixin extends BrewingStandContainerExtra {
}
