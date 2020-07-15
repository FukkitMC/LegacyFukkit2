package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.BeaconContainerExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link BeaconContainerExtra}
 */
@Mixin(net.minecraft.container.BeaconContainer.class)
public interface BeaconContainerExtraMixin extends BeaconContainerExtra {
}
