package org.bukkit.craftbukkit.block;

import net.minecraft.block.entity.FurnaceBlockEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventoryFurnace;
import org.bukkit.inventory.FurnaceInventory;

public class CraftFurnace extends CraftBlockState implements Furnace {
    private final FurnaceBlockEntity furnace;

    public CraftFurnace(final Block block) {
        super(block);

        furnace = (FurnaceBlockEntity) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftFurnace(final Material material, final FurnaceBlockEntity te) {
        super(material);
        furnace = te;
    }

    public FurnaceInventory getInventory() {
        return new CraftInventoryFurnace(furnace);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            furnace.markDirty();
        }

        return result;
    }

    public short getBurnTime() {
        return (short) furnace.field_1447;
    }

    public void setBurnTime(short burnTime) {
        furnace.field_1447 = burnTime;
    }

    public short getCookTime() {
        return (short) furnace.field_1449;
    }

    public void setCookTime(short cookTime) {
        furnace.field_1449 = cookTime;
    }

    @Override
    public FurnaceBlockEntity getTileEntity() {
        return furnace;
    }
}
