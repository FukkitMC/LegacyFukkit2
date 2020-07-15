package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.ChestBlockEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link ChestBlockEntityExtra}
 */
@Mixin(net.minecraft.block.entity.ChestBlockEntity.class)
public interface ChestBlockEntityExtraMixin extends ChestBlockEntityExtra {
}
