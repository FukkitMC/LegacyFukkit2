package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.LevelPropertiesExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link LevelPropertiesExtra}
 */
@Mixin(net.minecraft.world.level.LevelProperties.class)
public interface LevelPropertiesExtraMixin extends LevelPropertiesExtra {
}
