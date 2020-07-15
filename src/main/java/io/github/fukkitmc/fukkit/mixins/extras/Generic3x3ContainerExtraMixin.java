package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.Generic3x3ContainerExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link Generic3x3ContainerExtra}
 */
@Mixin(net.minecraft.container.Generic3x3Container.class)
public interface Generic3x3ContainerExtraMixin extends Generic3x3ContainerExtra {
}
