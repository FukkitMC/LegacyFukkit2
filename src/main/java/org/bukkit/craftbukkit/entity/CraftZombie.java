package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.mob.ZombieEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

public class CraftZombie extends CraftMonster implements Zombie {

    public CraftZombie(CraftServer server, ZombieEntity entity) {
        super(server, entity);
    }

    @Override
    public ZombieEntity getHandle() {
        return (ZombieEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftZombie";
    }

    public EntityType getType() {
        return EntityType.ZOMBIE;
    }

    public boolean isBaby() {
        return getHandle().method_7163();
    }

    public void setBaby(boolean flag) {
        getHandle().setBaby(flag);
    }

    public boolean isVillager() {
        return getHandle().method_7908();
    }

    public void setVillager(boolean flag) {
        getHandle().method_7913(flag);
    }
}
