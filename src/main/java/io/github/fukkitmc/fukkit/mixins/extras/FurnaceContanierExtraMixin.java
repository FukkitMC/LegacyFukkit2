package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.FurnaceContanierExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link FurnaceContanierExtra}
 */
@Mixin(net.minecraft.container.FurnaceContanier.class)
public interface FurnaceContanierExtraMixin extends FurnaceContanierExtra {
}
