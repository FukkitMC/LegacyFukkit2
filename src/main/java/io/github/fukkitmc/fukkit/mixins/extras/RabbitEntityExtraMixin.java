package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.RabbitEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link RabbitEntityExtra}
 */
@Mixin(net.minecraft.entity.passive.RabbitEntity.class)
public interface RabbitEntityExtraMixin extends RabbitEntityExtra {
}
