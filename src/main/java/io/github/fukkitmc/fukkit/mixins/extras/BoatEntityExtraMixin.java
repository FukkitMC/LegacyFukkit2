package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.BoatEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link BoatEntityExtra}
 */
@Mixin(net.minecraft.entity.vehicle.BoatEntity.class)
public interface BoatEntityExtraMixin extends BoatEntityExtra {
}
