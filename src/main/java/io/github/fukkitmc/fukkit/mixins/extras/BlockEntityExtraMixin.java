package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.BlockEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link BlockEntityExtra}
 */
@Mixin(net.minecraft.block.entity.BlockEntity.class)
public interface BlockEntityExtraMixin extends BlockEntityExtra {
}
