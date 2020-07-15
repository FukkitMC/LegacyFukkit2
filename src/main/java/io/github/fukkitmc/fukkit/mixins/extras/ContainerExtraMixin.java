package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.ContainerExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link ContainerExtra}
 */
@Mixin(net.minecraft.container.Container.class)
public interface ContainerExtraMixin extends ContainerExtra {
}
