package io.github.fukkitmc.legacy.mixins.extra;


import io.github.fukkitmc.legacy.extra.EntityFireballExtra;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ProjectileEntity.class)
public class EntityFireballMixin implements EntityFireballExtra {
    @Override
    public void setDirection(double d0, double d1, double d2) {

    }
}
