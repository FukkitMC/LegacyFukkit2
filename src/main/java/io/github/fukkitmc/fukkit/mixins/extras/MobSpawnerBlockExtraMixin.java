package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.MobSpawnerBlockExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link MobSpawnerBlockExtra}
 */
@Mixin(net.minecraft.block.MobSpawnerBlock.class)
public interface MobSpawnerBlockExtraMixin extends MobSpawnerBlockExtra {
}
