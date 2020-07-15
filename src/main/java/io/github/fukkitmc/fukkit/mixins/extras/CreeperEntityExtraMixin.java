package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.CreeperEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link CreeperEntityExtra}
 */
@Mixin(net.minecraft.entity.mob.CreeperEntity.class)
public interface CreeperEntityExtraMixin extends CreeperEntityExtra {
}
