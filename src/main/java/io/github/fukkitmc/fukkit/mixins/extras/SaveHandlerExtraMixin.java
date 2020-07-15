package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.SaveHandlerExtra;
import net.minecraft.world.SaveHandler;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link SaveHandlerExtra}
 */
@Mixin(SaveHandler.class)
public interface SaveHandlerExtraMixin extends SaveHandlerExtra {
}
