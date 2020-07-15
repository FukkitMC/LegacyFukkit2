package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.EnchantingTableContainerExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link EnchantingTableContainerExtra}
 */
@Mixin(net.minecraft.container.EnchantingTableContainer.class)
public interface EnchantingTableContainerExtraMixin extends EnchantingTableContainerExtra {
}
