package io.github.fukkitmc.legacy.mixins.extra;


import io.github.fukkitmc.legacy.extra.EntityMinecartAbstractExtra;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import org.bukkit.util.Vector;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractMinecartEntity.class)
public class EntityMinecartAbstractMixin implements EntityMinecartAbstractExtra {
    @Override
    public Vector getFlyingVelocityMod() {
        return null;
    }

    @Override
    public void setFlyingVelocityMod(Vector flying) {

    }

    @Override
    public Vector getDerailedVelocityMod() {
        return null;
    }

    @Override
    public void setDerailedVelocityMod(Vector derailed) {

    }
}
