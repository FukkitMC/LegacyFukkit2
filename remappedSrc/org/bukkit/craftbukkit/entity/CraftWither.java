package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.boss.WitherEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Wither;
import org.bukkit.entity.EntityType;

public class CraftWither extends CraftMonster implements Wither {
    public CraftWither(CraftServer server, WitherEntity entity) {
        super(server, entity);
    }

    @Override
    public WitherEntity getHandle() {
        return (WitherEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftWither";
    }

    public EntityType getType() {
        return EntityType.WITHER;
    }
}
