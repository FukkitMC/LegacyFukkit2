package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.BrewingStandBlockEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link BrewingStandBlockEntityExtra}
 */
@Mixin(net.minecraft.block.entity.BrewingStandBlockEntity.class)
public interface BrewingStandBlockEntityExtraMixin extends BrewingStandBlockEntityExtra {
}
