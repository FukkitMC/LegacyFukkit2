package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.CraftingResultInventoryExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link CraftingResultInventoryExtra}
 */
@Mixin(net.minecraft.inventory.CraftingResultInventory.class)
public interface CraftingResultInventoryExtraMixin extends CraftingResultInventoryExtra {
}
