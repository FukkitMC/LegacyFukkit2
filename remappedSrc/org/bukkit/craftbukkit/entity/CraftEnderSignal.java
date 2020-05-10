package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.thrown.EyeOfEnderEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.EntityType;

public class CraftEnderSignal extends CraftEntity implements EnderSignal {
    public CraftEnderSignal(CraftServer server, EyeOfEnderEntity entity) {
        super(server, entity);
    }

    @Override
    public EyeOfEnderEntity getHandle() {
        return (EyeOfEnderEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderSignal";
    }

    public EntityType getType() {
        return EntityType.ENDER_SIGNAL;
    }
}