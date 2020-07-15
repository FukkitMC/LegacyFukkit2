package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.AbstractMinecartEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link AbstractMinecartEntityExtra}
 */
@Mixin(net.minecraft.entity.vehicle.AbstractMinecartEntity.class)
public interface AbstractMinecartEntityExtraMixin extends AbstractMinecartEntityExtra {
}
