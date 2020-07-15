package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.DispenserBlockEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link DispenserBlockEntityExtra}
 */
@Mixin(net.minecraft.block.entity.DispenserBlockEntity.class)
public interface DispenserBlockEntityExtraMixin extends DispenserBlockEntityExtra {
}
