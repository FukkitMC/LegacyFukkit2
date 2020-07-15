package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.DoubleInventoryExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link DoubleInventoryExtra}
 */
@Mixin(net.minecraft.inventory.DoubleInventory.class)
public interface DoubleInventoryExtraMixin extends DoubleInventoryExtra {
}
