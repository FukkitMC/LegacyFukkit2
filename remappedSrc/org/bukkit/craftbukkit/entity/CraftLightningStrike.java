package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.LightningBoltEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LightningStrike;

public class CraftLightningStrike extends CraftEntity implements LightningStrike {
    public CraftLightningStrike(final CraftServer server, final LightningBoltEntity entity) {
        super(server, entity);
    }

    public boolean isEffect() {
        return ((LightningBoltEntity) super.getHandle()).isEffect;
    }

    @Override
    public LightningBoltEntity getHandle() {
        return (LightningBoltEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftLightningStrike";
    }

    public EntityType getType() {
        return EntityType.LIGHTNING;
    }
}
