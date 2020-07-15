package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.InventoryMinecartEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link InventoryMinecartEntityExtra}
 */
@Mixin(net.minecraft.entity.vehicle.InventoryMinecartEntity.class)
public interface InventoryMinecartEntityExtraMixin extends InventoryMinecartEntityExtra {
}
