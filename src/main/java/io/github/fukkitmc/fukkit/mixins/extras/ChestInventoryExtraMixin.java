package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.ChestInventoryExtra;
import net.minecraft.container.ChestInventory;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link ChestInventoryExtra}
 */
@Mixin(ChestInventory.class)
public interface ChestInventoryExtraMixin extends ChestInventoryExtra {
}
