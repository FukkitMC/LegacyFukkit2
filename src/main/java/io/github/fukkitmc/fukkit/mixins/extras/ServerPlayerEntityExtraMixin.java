package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.ServerPlayerEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link ServerPlayerEntityExtra}
 */
@Mixin(net.minecraft.entity.player.ServerPlayerEntity.class)
public interface ServerPlayerEntityExtraMixin extends ServerPlayerEntityExtra {
}
