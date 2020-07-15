package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.WorldExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link WorldExtra}
 */
@Mixin(net.minecraft.world.World.class)
public interface WorldExtraMixin extends WorldExtra {
}
