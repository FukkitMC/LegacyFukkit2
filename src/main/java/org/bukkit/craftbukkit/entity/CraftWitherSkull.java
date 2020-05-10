package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.projectile.WitherSkullEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkull;

public class CraftWitherSkull extends CraftFireball implements WitherSkull {
    public CraftWitherSkull(CraftServer server, WitherSkullEntity entity) {
        super(server, entity);
    }

    @Override
    public void setCharged(boolean charged) {
        getHandle().setCharged(charged);
    }

    @Override
    public boolean isCharged() {
        return getHandle().isCharged();
    }

    @Override
    public WitherSkullEntity getHandle() {
        return (WitherSkullEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftWitherSkull";
    }

    public EntityType getType() {
        return EntityType.WITHER_SKULL;
    }
}
