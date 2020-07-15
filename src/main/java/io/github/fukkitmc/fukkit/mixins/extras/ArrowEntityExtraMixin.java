package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.ArrowEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link ArrowEntityExtra}
 */
@Mixin(net.minecraft.entity.projectile.ArrowEntity.class)
public interface ArrowEntityExtraMixin extends ArrowEntityExtra {
}
