package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.CraftingTableContainerExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link CraftingTableContainerExtra}
 */
@Mixin(net.minecraft.container.CraftingTableContainer.class)
public interface CraftingTableContainerExtraMixin extends CraftingTableContainerExtra {
}
