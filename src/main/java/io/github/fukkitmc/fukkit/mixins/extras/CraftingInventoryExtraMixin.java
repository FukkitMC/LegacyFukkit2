package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.CraftingInventoryExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link CraftingInventoryExtra}
 */
@Mixin(net.minecraft.inventory.CraftingInventory.class)
public interface CraftingInventoryExtraMixin extends CraftingInventoryExtra {
}
