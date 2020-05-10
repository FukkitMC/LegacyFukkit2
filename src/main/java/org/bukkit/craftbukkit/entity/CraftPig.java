package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.PigEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;

public class CraftPig extends CraftAnimals implements Pig {
    public CraftPig(CraftServer server, PigEntity entity) {
        super(server, entity);
    }

    public boolean hasSaddle() {
        return getHandle().isSaddled();
    }

    public void setSaddle(boolean saddled) {
        getHandle().setSaddle(saddled);
    }

    public PigEntity getHandle() {
        return (PigEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftPig";
    }

    public EntityType getType() {
        return EntityType.PIG;
    }
}
