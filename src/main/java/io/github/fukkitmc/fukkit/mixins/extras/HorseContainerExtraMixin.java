package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.HorseContainerExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link HorseContainerExtra}
 */
@Mixin(net.minecraft.container.HorseContainer.class)
public interface HorseContainerExtraMixin extends HorseContainerExtra {
}
