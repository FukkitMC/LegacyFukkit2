package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.FurnaceBlockEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link FurnaceBlockEntityExtra}
 */
@Mixin(net.minecraft.block.entity.FurnaceBlockEntity.class)
public interface FurnaceBlockEntityExtraMixin extends FurnaceBlockEntityExtra {
}
