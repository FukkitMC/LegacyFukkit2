package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.EndCrystalEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;

public class CraftEnderCrystal extends CraftEntity implements EnderCrystal {
    public CraftEnderCrystal(CraftServer server, EndCrystalEntity entity) {
        super(server, entity);
    }

    @Override
    public EndCrystalEntity getHandle() {
        return (EndCrystalEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderCrystal";
    }

    public EntityType getType() {
        return EntityType.ENDER_CRYSTAL;
    }
}
