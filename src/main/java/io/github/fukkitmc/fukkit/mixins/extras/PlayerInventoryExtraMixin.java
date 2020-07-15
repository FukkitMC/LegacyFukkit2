package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.PlayerInventoryExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link PlayerInventoryExtra}
 */
@Mixin(net.minecraft.entity.player.PlayerInventory.class)
public interface PlayerInventoryExtraMixin extends PlayerInventoryExtra {
}
