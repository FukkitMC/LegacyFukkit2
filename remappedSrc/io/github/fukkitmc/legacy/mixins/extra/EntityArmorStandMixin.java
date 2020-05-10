package io.github.fukkitmc.legacy.mixins.extra;


import io.github.fukkitmc.legacy.extra.EntityArmorStandExtra;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArmorStandEntity.class)
public class EntityArmorStandMixin implements EntityArmorStandExtra {
    @Override
    public void n(boolean flag) {

    }
}
