package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.mob.WitchEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Witch;
import org.bukkit.entity.EntityType;

public class CraftWitch extends CraftMonster implements Witch {
    public CraftWitch(CraftServer server, WitchEntity entity) {
        super(server, entity);
    }

    @Override
    public WitchEntity getHandle() {
        return (WitchEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftWitch";
    }

    public EntityType getType() {
        return EntityType.WITCH;
    }
}
