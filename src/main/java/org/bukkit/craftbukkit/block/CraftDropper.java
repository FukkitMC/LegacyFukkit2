package org.bukkit.craftbukkit.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.DropperBlock;
import net.minecraft.block.entity.DropperBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dropper;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;

public class CraftDropper extends CraftBlockState implements Dropper {
    private final CraftWorld world;
    private final DropperBlockEntity dropper;

    public CraftDropper(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        dropper = (DropperBlockEntity) world.getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftDropper(final Material material, DropperBlockEntity te) {
        super(material);
        world = null;
        dropper = te;
    }

    public Inventory getInventory() {
        return new CraftInventory(dropper);
    }

    public void drop() {
        Block block = getBlock();

        if (block.getType() == Material.DROPPER) {
            DropperBlock drop = (DropperBlock) Blocks.DROPPER;

            drop.dispense(world.getHandle(), new BlockPos(getX(), getY(), getZ()));
        }
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result) {
            dropper.markDirty();
        }

        return result;
    }

    @Override
    public DropperBlockEntity getTileEntity() {
        return dropper;
    }
}
