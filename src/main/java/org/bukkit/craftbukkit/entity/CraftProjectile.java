package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.thrown.ThrowableEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public abstract class CraftProjectile extends AbstractProjectile implements Projectile {
    public CraftProjectile(CraftServer server, net.minecraft.entity.Entity entity) {
        super(server, entity);
    }

    public ProjectileSource getShooter() {
        return getHandle().projectileSource;
    }

    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            getHandle().field_8425 = (net.minecraft.entity.LivingEntity) ((CraftLivingEntity) shooter).entity;
            if (shooter instanceof CraftHumanEntity) {
                getHandle().field_8426 = ((CraftHumanEntity) shooter).getName();
            }
        } else {
            getHandle().field_8425 = null;
            getHandle().field_8426 = null;
        }
        getHandle().projectileSource = shooter;
    }

    @Override
    public ThrowableEntity getHandle() {
        return (ThrowableEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftProjectile";
    }


    @Deprecated
    public LivingEntity _INVALID_getShooter() {
        if (getHandle().field_8425 == null) {
            return null;
        }
        return (LivingEntity) getHandle().field_8425.getBukkitEntity();
    }

    @Deprecated
    public void _INVALID_setShooter(LivingEntity shooter) {
        if (shooter == null) {
            return;
        }
        getHandle().field_8425 = ((CraftLivingEntity) shooter).getHandle();
        if (shooter instanceof CraftHumanEntity) {
            getHandle().field_8426 = ((CraftHumanEntity) shooter).getName();
        }
    }
}
