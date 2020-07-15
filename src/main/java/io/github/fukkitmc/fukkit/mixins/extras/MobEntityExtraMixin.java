package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.MobEntityExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link MobEntityExtra}
 */
@Mixin(net.minecraft.entity.mob.MobEntity.class)
public interface MobEntityExtraMixin extends MobEntityExtra {
}
