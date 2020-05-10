package org.bukkit.craftbukkit.block;

import net.minecraft.block.entity.HopperBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftHopper extends CraftBlockState implements Hopper {
    private final HopperBlockEntity hopper;

    public CraftHopper(final Block block) {
        super(block);

        hopper = (HopperBlockEntity) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftHopper(final Material material, final HopperBlockEntity te) {
        super(material);

        hopper = te;
    }

    public Inventory getInventory() {
        return new CraftInventory(hopper);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            hopper.markDirty();
        }

        return result;
    }

    @Override
    public HopperBlockEntity getTileEntity() {
        return hopper;
    }
}
