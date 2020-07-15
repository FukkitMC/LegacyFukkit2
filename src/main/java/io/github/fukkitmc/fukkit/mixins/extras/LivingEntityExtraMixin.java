package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.LivingEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link LivingEntityExtra}
 */
@Mixin(net.minecraft.entity.LivingEntity.class)
public interface LivingEntityExtraMixin extends LivingEntityExtra {
}
