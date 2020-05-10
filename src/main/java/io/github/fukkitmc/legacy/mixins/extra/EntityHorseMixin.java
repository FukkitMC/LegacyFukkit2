package io.github.fukkitmc.legacy.mixins.extra;


import io.github.fukkitmc.legacy.extra.EntityHorseExtra;
import net.minecraft.entity.passive.HorseBaseEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HorseBaseEntity.class)
public class EntityHorseMixin implements EntityHorseExtra {
}
