package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.AnvilContainerExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link AnvilContainerExtra}
 */
@Mixin(net.minecraft.container.AnvilContainer.class)
public interface AnvilContainerExtraMixin extends AnvilContainerExtra {
}
