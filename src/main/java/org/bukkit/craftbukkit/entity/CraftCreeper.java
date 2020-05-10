package org.bukkit.craftbukkit.entity;

import io.github.fukkitmc.legacy.extra.EntityCreeperExtra;
import io.github.fukkitmc.legacy.extra.EntityExtra;
import net.minecraft.entity.mob.CreeperEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreeperPowerEvent;

public class CraftCreeper extends CraftMonster implements Creeper {

    public CraftCreeper(CraftServer server, CreeperEntity entity) {
        super(server, entity);
    }

    public boolean isPowered() {
        return getHandle().isPowered();
    }

    public void setPowered(boolean powered) {
        CraftServer server = this.server;
        Creeper entity = (Creeper) ((EntityExtra)this.getHandle()).getBukkitEntity();

        if (powered) {
            CreeperPowerEvent event = new CreeperPowerEvent(entity, CreeperPowerEvent.PowerCause.SET_ON);
            server.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                ((EntityCreeperExtra)getHandle()).setPowered(true);
            }
        } else {
            CreeperPowerEvent event = new CreeperPowerEvent(entity, CreeperPowerEvent.PowerCause.SET_OFF);
            server.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                ((EntityCreeperExtra)getHandle()).setPowered(false);
            }
        }
    }

    @Override
    public CreeperEntity getHandle() {
        return (CreeperEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftCreeper";
    }

    public EntityType getType() {
        return EntityType.CREEPER;
    }
}
