package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.MerchantContainerExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link MerchantContainerExtra}
 */
@Mixin(net.minecraft.container.MerchantContainer.class)
public interface MerchantContainerExtraMixin extends MerchantContainerExtra {
}
