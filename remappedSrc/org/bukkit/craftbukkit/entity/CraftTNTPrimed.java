package org.bukkit.craftbukkit.entity;

import io.github.fukkitmc.legacy.extra.EntityExtra;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;

public class CraftTNTPrimed extends CraftEntity implements TNTPrimed {

    public CraftTNTPrimed(CraftServer server, TntEntity entity) {
        super(server, entity);
    }

    public float getYield() {
        return getHandle().yield;
    }

    public boolean isIncendiary() {
        return getHandle().isIncendiary;
    }

    public void setIsIncendiary(boolean isIncendiary) {
        getHandle().isIncendiary = isIncendiary;
    }

    public void setYield(float yield) {
        getHandle().yield = yield;
    }

    public int getFuseTicks() {
        return getHandle().fuseTimer;
    }

    public void setFuseTicks(int fuseTicks) {
        getHandle().fuseTimer = fuseTicks;
    }

    @Override
    public TntEntity getHandle() {
        return (TntEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftTNTPrimed";
    }

    public EntityType getType() {
        return EntityType.PRIMED_TNT;
    }

    public Entity getSource() {
        LivingEntity source = getHandle().getSource();

        if (source != null) {
            Entity bukkitEntity = ((EntityExtra)source).getBukkitEntity();

            if (bukkitEntity.isValid()) {
                return bukkitEntity;
            }
        }

        return null;
    }
}
