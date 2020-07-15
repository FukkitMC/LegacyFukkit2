package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.projectile.ArrowEntity;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.projectiles.ProjectileSource;

public class CraftArrow extends AbstractProjectile implements Arrow {

    public CraftArrow(CraftServer server, ArrowEntity entity) {
        super(server, entity);
    }

    public void setKnockbackStrength(int knockbackStrength) {
        Validate.isTrue(knockbackStrength >= 0, "Knockback cannot be negative");
        getHandle().method_8059(knockbackStrength);
    }

    public int getKnockbackStrength() {
        return getHandle().field_8390;
    }

    public boolean isCritical() {
        return getHandle().method_8063();
    }

    public void setCritical(boolean critical) {
        getHandle().method_8060(critical);
    }

    public ProjectileSource getShooter() {
        return getHandle().projectileSource;   
    }

    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof LivingEntity) {
            getHandle().field_8392 = ((CraftLivingEntity) shooter).getHandle();
        } else {
            getHandle().field_8392 = null;
        }
        getHandle().projectileSource = shooter;
    }

    @Override
    public ArrowEntity getHandle() {
        return (ArrowEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftArrow";
    }

    public EntityType getType() {
        return EntityType.ARROW;
    }

    @Deprecated
    public LivingEntity _INVALID_getShooter() {
        if (getHandle().field_8392 == null) {
            return null;
        }
        return (LivingEntity) getHandle().field_8392.getBukkitEntity();
    }

    @Deprecated
    public void _INVALID_setShooter(LivingEntity shooter) {
        getHandle().field_8392 = ((CraftLivingEntity) shooter).getHandle();
    }

    // Spigot start
    private final Arrow.Spigot spigot = new Arrow.Spigot()
    {
        @Override
        public double getDamage()
        {
            return getHandle().method_8062();
        }

        @Override
        public void setDamage(double damage)
        {
            getHandle().method_8061( damage );
        }
    };

    public Arrow.Spigot spigot()
    {
        return spigot;
    }
    // Spigot end
}
