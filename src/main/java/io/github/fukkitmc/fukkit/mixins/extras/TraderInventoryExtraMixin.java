package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.TraderInventoryExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link TraderInventoryExtra}
 */
@Mixin(net.minecraft.village.TraderInventory.class)
public interface TraderInventoryExtraMixin extends TraderInventoryExtra {
}
