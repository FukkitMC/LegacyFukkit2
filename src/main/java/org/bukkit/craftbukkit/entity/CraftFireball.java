package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class CraftFireball extends AbstractProjectile implements Fireball {
    public CraftFireball(CraftServer server, ProjectileEntity entity) {
        super(server, entity);
    }

    public float getYield() {
        return getHandle().bukkitYield;
    }

    public boolean isIncendiary() {
        return getHandle().isIncendiary;
    }

    public void setIsIncendiary(boolean isIncendiary) {
        getHandle().isIncendiary = isIncendiary;
    }

    public void setYield(float yield) {
        getHandle().bukkitYield = yield;
    }

    public ProjectileSource getShooter() {
        return getHandle().projectileSource;
    }

    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            getHandle().field_8404 = ((CraftLivingEntity) shooter).getHandle();
        } else {
            getHandle().field_8404 = null;
        }
        getHandle().projectileSource = shooter;
    }

    public Vector getDirection() {
        return new Vector(getHandle().field_8407, getHandle().field_8408, getHandle().field_8409);
    }

    public void setDirection(Vector direction) {
        Validate.notNull(direction, "Direction can not be null");
        double x = direction.getX();
        double y = direction.getY();
        double z = direction.getZ();
        double magnitude = (double) MathHelper.sqrt(x * x + y * y + z * z);
        getHandle().field_8407 = x / magnitude;
        getHandle().field_8408 = y / magnitude;
        getHandle().field_8409 = z / magnitude;
    }

    @Override
    public ProjectileEntity getHandle() {
        return (ProjectileEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftFireball";
    }

    public EntityType getType() {
        return EntityType.UNKNOWN;
    }

    @Deprecated
    public void _INVALID_setShooter(LivingEntity shooter) {
        setShooter(shooter);
    }

    @Deprecated
    public LivingEntity _INVALID_getShooter() {
        if (getHandle().field_8404 != null) {
            return (LivingEntity) getHandle().field_8404.getBukkitEntity();
        }
        return null;
    }
}
