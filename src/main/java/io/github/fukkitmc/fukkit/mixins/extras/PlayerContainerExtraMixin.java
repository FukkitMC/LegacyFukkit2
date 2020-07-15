package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.PlayerContainerExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link PlayerContainerExtra}
 */
@Mixin(net.minecraft.container.PlayerContainer.class)
public interface PlayerContainerExtraMixin extends PlayerContainerExtra {
}
