package org.bukkit.craftbukkit.block;

import net.minecraft.block.entity.MobSpawnerBlockEntity;
import org.bukkit.Material;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EntityType;

public class CraftCreatureSpawner extends CraftBlockState implements CreatureSpawner {
    private final MobSpawnerBlockEntity spawner;

    public CraftCreatureSpawner(final Block block) {
        super(block);

        spawner = (MobSpawnerBlockEntity) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftCreatureSpawner(final Material material, MobSpawnerBlockEntity te) {
        super(material);
        spawner = te;
    }

    @Deprecated
    public CreatureType getCreatureType() {
        return CreatureType.fromName(spawner.method_1174().method_185());
    }

    public EntityType getSpawnedType() {
        return EntityType.fromName(spawner.method_1174().method_185());
    }

    @Deprecated
    public void setCreatureType(CreatureType creatureType) {
        spawner.method_1174().method_177(creatureType.getName());
    }

    public void setSpawnedType(EntityType entityType) {
        if (entityType == null || entityType.getName() == null) {
            throw new IllegalArgumentException("Can't spawn EntityType " + entityType + " from mobspawners!");
        }

        spawner.method_1174().method_177(entityType.getName());
    }

    @Deprecated
    public String getCreatureTypeId() {
        return spawner.method_1174().method_185();
    }

    @Deprecated
    public void setCreatureTypeId(String creatureName) {
        setCreatureTypeByName(creatureName);
    }

    public String getCreatureTypeName() {
        return spawner.method_1174().method_185();
    }

    public void setCreatureTypeByName(String creatureType) {
        // Verify input
        EntityType type = EntityType.fromName(creatureType);
        if (type == null) {
            return;
        }
        setSpawnedType(type);
    }

    public int getDelay() {
        return spawner.method_1174().field_198;
    }

    public void setDelay(int delay) {
        spawner.method_1174().field_198 = delay;
    }

    @Override
    public MobSpawnerBlockEntity getTileEntity() {
        return spawner;
    }
}
