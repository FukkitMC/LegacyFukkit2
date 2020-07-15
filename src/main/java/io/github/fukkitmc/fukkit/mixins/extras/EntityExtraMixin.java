package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.EntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link EntityExtra}
 */
@Mixin(net.minecraft.entity.Entity.class)
public interface EntityExtraMixin extends EntityExtra {
}
