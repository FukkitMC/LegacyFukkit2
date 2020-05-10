package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.thrown.ExperienceBottleEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ThrownExpBottle;

public class CraftThrownExpBottle extends CraftProjectile implements ThrownExpBottle {
    public CraftThrownExpBottle(CraftServer server, ExperienceBottleEntity entity) {
        super(server, entity);
    }

    @Override
    public ExperienceBottleEntity getHandle() {
        return (ExperienceBottleEntity) entity;
    }

    @Override
    public String toString() {
        return "EntityThrownExpBottle";
    }

    public EntityType getType() {
        return EntityType.THROWN_EXP_BOTTLE;
    }
}
