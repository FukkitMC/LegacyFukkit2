package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.ExperienceOrbEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;

public class CraftExperienceOrb extends CraftEntity implements ExperienceOrb {
    public CraftExperienceOrb(CraftServer server, ExperienceOrbEntity entity) {
        super(server, entity);
    }

    public int getExperience() {
        return getHandle().amount;
    }

    public void setExperience(int value) {
        getHandle().amount = value;
    }

    @Override
    public ExperienceOrbEntity getHandle() {
        return (ExperienceOrbEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftExperienceOrb";
    }

    public EntityType getType() {
        return EntityType.EXPERIENCE_ORB;
    }
}