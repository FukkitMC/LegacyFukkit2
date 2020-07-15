package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.HealthBoostStatusEffectExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link HealthBoostStatusEffectExtra}
 */
@Mixin(net.minecraft.entity.effect.HealthBoostStatusEffect.class)
public interface HealthBoostStatusEffectExtraMixin extends HealthBoostStatusEffectExtra {
}
