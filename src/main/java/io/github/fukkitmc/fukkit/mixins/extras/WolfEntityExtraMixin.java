package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.WolfEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link WolfEntityExtra}
 */
@Mixin(net.minecraft.entity.passive.WolfEntity.class)
public interface WolfEntityExtraMixin extends WolfEntityExtra {
}
