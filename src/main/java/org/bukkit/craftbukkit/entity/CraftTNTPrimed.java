package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.ExplodingTntEntity;
import net.minecraft.entity.LivingEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;

public class CraftTNTPrimed extends CraftEntity implements TNTPrimed {

    public CraftTNTPrimed(CraftServer server, ExplodingTntEntity entity) {
        super(server, entity);
    }

    public float getYield() {
        return 4;
//        return getHandle().yield;
    }

    public boolean isIncendiary() {
//        return getHandle().isIncendiary;
        return false;
    }

    public void setIsIncendiary(boolean isIncendiary) {
//        getHandle().isIncendiary = isIncendiary;
    }

    public void setYield(float yield) {
//        getHandle().yield = yield;
    }

    public int getFuseTicks() {
        return getHandle().fuseTimer;
    }

    public void setFuseTicks(int fuseTicks) {
        getHandle().fuseTimer = fuseTicks;
    }

    @Override
    public ExplodingTntEntity getHandle() {
        return (ExplodingTntEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftTNTPrimed";
    }

    public EntityType getType() {
        return EntityType.PRIMED_TNT;
    }

    public Entity getSource() {
        LivingEntity source = getHandle().method_7816();

        if (source != null) {
            Entity bukkitEntity = source.getBukkitEntity();

            if (bukkitEntity.isValid()) {
                return bukkitEntity;
            }
        }

        return null;
    }
}
