package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.SkullBlockEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link SkullBlockEntityExtra}
 */
@Mixin(net.minecraft.block.entity.SkullBlockEntity.class)
public interface SkullBlockEntityExtraMixin extends SkullBlockEntityExtra {
}
