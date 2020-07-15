package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.ProjectileEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link ProjectileEntityExtra}
 */
@Mixin(net.minecraft.entity.projectile.ProjectileEntity.class)
public interface ProjectileEntityExtraMixin extends ProjectileEntityExtra {
}
