package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.EnderChestInventoryExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link EnderChestInventoryExtra}
 */
@Mixin(net.minecraft.inventory.EnderChestInventory.class)
public interface EnderChestInventoryExtraMixin extends EnderChestInventoryExtra {
}
