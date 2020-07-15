package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.PlayerEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link PlayerEntityExtra}
 */
@Mixin(net.minecraft.entity.player.PlayerEntity.class)
public interface PlayerEntityExtraMixin extends PlayerEntityExtra {
}
