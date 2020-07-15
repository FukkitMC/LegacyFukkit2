package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.BeaconBlockEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link BeaconBlockEntityExtra}
 */
@Mixin(net.minecraft.block.entity.BeaconBlockEntity.class)
public interface BeaconBlockEntityExtraMixin extends BeaconBlockEntityExtra {
}
