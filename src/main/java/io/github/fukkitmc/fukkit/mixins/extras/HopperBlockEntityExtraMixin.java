package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.HopperBlockEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link HopperBlockEntityExtra}
 */
@Mixin(net.minecraft.block.entity.HopperBlockEntity.class)
public interface HopperBlockEntityExtraMixin extends HopperBlockEntityExtra {
}
