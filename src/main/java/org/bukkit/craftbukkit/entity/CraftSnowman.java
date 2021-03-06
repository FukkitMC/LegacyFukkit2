package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.passive.SnowGolemEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowman;

public class CraftSnowman extends CraftGolem implements Snowman {
    public CraftSnowman(CraftServer server, SnowGolemEntity entity) {
        super(server, entity);
    }

    @Override
    public SnowGolemEntity getHandle() {
        return (SnowGolemEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftSnowman";
    }

    public EntityType getType() {
        return EntityType.SNOWMAN;
    }
}
