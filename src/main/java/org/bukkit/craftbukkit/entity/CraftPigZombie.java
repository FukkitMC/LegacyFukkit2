package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.mob.ZombiePigmanEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;

public class CraftPigZombie extends CraftZombie implements PigZombie {

    public CraftPigZombie(CraftServer server, ZombiePigmanEntity entity) {
        super(server, entity);
    }

    public int getAnger() {
        return getHandle().anger;
    }

    public void setAnger(int level) {
        getHandle().anger = level;
    }

    public void setAngry(boolean angry) {
        setAnger(angry ? 400 : 0);
    }

    public boolean isAngry() {
        return getAnger() > 0;
    }

    @Override
    public ZombiePigmanEntity getHandle() {
        return (ZombiePigmanEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftPigZombie";
    }

    public EntityType getType() {
        return EntityType.PIG_ZOMBIE;
    }
}
