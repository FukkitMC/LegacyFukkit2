package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.mob.MagmaCubeEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;

public class CraftMagmaCube extends CraftSlime implements MagmaCube {

    public CraftMagmaCube(CraftServer server, MagmaCubeEntity entity) {
        super(server, entity);
    }

    public MagmaCubeEntity getHandle() {
        return (MagmaCubeEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftMagmaCube";
    }

    public EntityType getType() {
        return EntityType.MAGMA_CUBE;
    }
}
