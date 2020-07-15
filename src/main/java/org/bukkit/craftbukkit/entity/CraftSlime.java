package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.mob.SlimeEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;

public class CraftSlime extends CraftLivingEntity implements Slime {

    public CraftSlime(CraftServer server, SlimeEntity entity) {
        super(server, entity);
    }

    public int getSize() {
        return getHandle().getSize();
    }

    public void setSize(int size) {
        getHandle().method_7885(size);
    }

    @Override
    public SlimeEntity getHandle() {
        return (SlimeEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftSlime";
    }

    public EntityType getType() {
        return EntityType.SLIME;
    }
}
