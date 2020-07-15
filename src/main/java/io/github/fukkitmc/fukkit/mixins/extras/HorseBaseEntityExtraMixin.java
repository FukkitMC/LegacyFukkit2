package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.HorseBaseEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link HorseBaseEntityExtra}
 */
@Mixin(net.minecraft.entity.passive.HorseBaseEntity.class)
public interface HorseBaseEntityExtraMixin extends HorseBaseEntityExtra {
}
