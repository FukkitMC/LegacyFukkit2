package org.bukkit.craftbukkit.entity;

import net.minecraft.entity.FallingBlockEntity;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingSand;

public class CraftFallingSand extends CraftEntity implements FallingSand {

    public CraftFallingSand(CraftServer server, FallingBlockEntity entity) {
        super(server, entity);
    }

    @Override
    public FallingBlockEntity getHandle() {
        return (FallingBlockEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftFallingSand";
    }

    public EntityType getType() {
        return EntityType.FALLING_BLOCK;
    }

    public Material getMaterial() {
        return Material.getMaterial(getBlockId());
    }

    public int getBlockId() {
        return CraftMagicNumbers.getId(getHandle().getBlock().getBlock());
    }

    public byte getBlockData() {
        return (byte) getHandle().getBlock().getBlock().getData(getHandle().getBlock());
    }

    public boolean getDropItem() {
        return getHandle().dropping;
    }

    public void setDropItem(boolean drop) {
        getHandle().dropping = drop;
    }

    @Override
    public boolean canHurtEntities() {
        return getHandle().hurtingEntities;
    }

    @Override
    public void setHurtEntities(boolean hurtEntities) {
        getHandle().hurtingEntities = hurtEntities;
    }
}
