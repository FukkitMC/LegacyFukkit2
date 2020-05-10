package org.bukkit.craftbukkit.block;

import net.minecraft.block.entity.BrewingStandBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventoryBrewer;
import org.bukkit.inventory.BrewerInventory;

public class CraftBrewingStand extends CraftBlockState implements BrewingStand {
    private final BrewingStandBlockEntity brewingStand;

    public CraftBrewingStand(Block block) {
        super(block);

        brewingStand = (BrewingStandBlockEntity) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftBrewingStand(final Material material, final BrewingStandBlockEntity te) {
        super(material);
        brewingStand = te;
    }

    public BrewerInventory getInventory() {
        return new CraftInventoryBrewer(brewingStand);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            brewingStand.markDirty();
        }

        return result;
    }

    public int getBrewingTime() {
        return brewingStand.brewTime;
    }

    public void setBrewingTime(int brewTime) {
        brewingStand.brewTime = brewTime;
    }

    @Override
    public BrewingStandBlockEntity getTileEntity() {
        return brewingStand;
    }
}
