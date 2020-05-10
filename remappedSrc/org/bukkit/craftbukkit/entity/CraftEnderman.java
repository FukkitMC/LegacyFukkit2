package org.bukkit.craftbukkit.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.EndermanEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;

public class CraftEnderman extends CraftMonster implements Enderman {
    public CraftEnderman(CraftServer server, EndermanEntity entity) {
        super(server, entity);
    }

    public MaterialData getCarriedMaterial() {
        BlockState blockData = getHandle().getCarriedBlock();
        return CraftMagicNumbers.getMaterial(blockData.getBlock()).getNewData((byte) blockData.getBlock().getData(blockData));
    }

    public void setCarriedMaterial(MaterialData data) {
        getHandle().setCarriedBlock(CraftMagicNumbers.getBlock(data.getItemTypeId()).stateFromData(data.getData()));
    }

    @Override
    public EndermanEntity getHandle() {
        return (EndermanEntity) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderman";
    }

    public EntityType getType() {
        return EntityType.ENDERMAN;
    }
}
