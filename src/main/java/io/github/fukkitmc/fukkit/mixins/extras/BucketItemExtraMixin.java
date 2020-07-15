package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.BucketItemExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link BucketItemExtra}
 */
@Mixin(net.minecraft.item.BucketItem.class)
public interface BucketItemExtraMixin extends BucketItemExtra {
}
