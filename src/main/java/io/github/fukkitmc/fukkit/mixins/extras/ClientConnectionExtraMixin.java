package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.ClientConnectionExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link ClientConnectionExtra}
 */
@Mixin(net.minecraft.network.ClientConnection.class)
public interface ClientConnectionExtraMixin extends ClientConnectionExtra {
}
