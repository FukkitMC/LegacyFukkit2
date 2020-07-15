package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.InventoryExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link InventoryExtra}
 */
@Mixin(net.minecraft.inventory.Inventory.class)
public interface InventoryExtraMixin extends InventoryExtra {
}
