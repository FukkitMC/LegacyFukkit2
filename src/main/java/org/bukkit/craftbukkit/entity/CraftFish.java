package org.bukkit.craftbukkit.entity;

import io.github.fukkitmc.legacy.extra.EntityExtra;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fish;
import org.bukkit.entity.LivingEntity;
import org.bukkit.projectiles.ProjectileSource;

public class CraftFish extends AbstractProjectile implements Fish {
    private double biteChance = -1;

    public CraftFish(CraftServer server, FishingBobberEntity entity) {
        super(server, entity);
    }

    public ProjectileSource getShooter() {
        if (getHandle().owner != null) {
            return (ProjectileSource) ((EntityExtra)getHandle().owner).getBukkitEntity();
        }

        return null;
    }

    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftHumanEntity) {
            getHandle().owner = (PlayerEntity) ((CraftHumanEntity) shooter).entity;
        }
    }

    @Override
    public FishingBobberEntity getHandle() {
        return (FishingBobberEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftFish";
    }

    public EntityType getType() {
        return EntityType.FISHING_HOOK;
    }

    public double getBiteChance() {
        FishingBobberEntity hook = getHandle();

        if (this.biteChance == -1) {
            if (hook.world.hasRain(new BlockPos(MathHelper.floor(hook.x), MathHelper.floor(hook.y) + 1, MathHelper.floor(hook.z)))) {
                return 1/300.0;
            }
            return 1/500.0;
        }
        return this.biteChance;
    }

    public void setBiteChance(double chance) {
        Validate.isTrue(chance >= 0 && chance <= 1, "The bite chance must be between 0 and 1.");
        this.biteChance = chance;
    }

    @Deprecated
    public LivingEntity _INVALID_getShooter() {
        return (LivingEntity) getShooter();
    }

    @Deprecated
    public void _INVALID_setShooter(LivingEntity shooter) {
        setShooter(shooter);
    }
}
