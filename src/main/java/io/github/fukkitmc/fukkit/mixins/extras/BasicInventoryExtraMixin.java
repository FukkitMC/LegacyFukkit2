package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.BasicInventoryExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link BasicInventoryExtra}
 */
@Mixin(net.minecraft.inventory.BasicInventory.class)
public interface BasicInventoryExtraMixin extends BasicInventoryExtra {
}
