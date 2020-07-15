package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.EnderDragonEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link EnderDragonEntityExtra}
 */
@Mixin(net.minecraft.entity.boss.dragon.EnderDragonEntity.class)
public interface EnderDragonEntityExtraMixin extends EnderDragonEntityExtra {
}
