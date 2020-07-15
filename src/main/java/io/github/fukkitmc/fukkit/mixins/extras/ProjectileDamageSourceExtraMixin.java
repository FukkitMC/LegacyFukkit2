package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.ProjectileDamageSourceExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link ProjectileDamageSourceExtra}
 */
@Mixin(net.minecraft.entity.damage.ProjectileDamageSource.class)
public interface ProjectileDamageSourceExtraMixin extends ProjectileDamageSourceExtra {
}
