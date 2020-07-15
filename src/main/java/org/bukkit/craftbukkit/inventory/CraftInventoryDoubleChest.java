package org.bukkit.craftbukkit.inventory;

import net.minecraft.block.entity.LockableContainerProvider;
import net.minecraft.inventory.DoubleInventory;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryDoubleChest extends CraftInventory implements DoubleChestInventory {
    private final CraftInventory left;
    private final CraftInventory right;

    public CraftInventoryDoubleChest(CraftInventory left, CraftInventory right) {
        super(new DoubleInventory("Large chest", (LockableContainerProvider) left.getInventory(), (LockableContainerProvider) right.getInventory()));
        this.left = left;
        this.right = right;
    }

    public CraftInventoryDoubleChest(DoubleInventory largeChest) {
        super(largeChest);
        if (largeChest.field_7201 instanceof DoubleInventory) {
            left = new CraftInventoryDoubleChest((DoubleInventory) largeChest.field_7201);
        } else {
            left = new CraftInventory(largeChest.field_7201);
        }
        if (largeChest.field_7202 instanceof DoubleInventory) {
            right = new CraftInventoryDoubleChest((DoubleInventory) largeChest.field_7202);
        } else {
            right = new CraftInventory(largeChest.field_7202);
        }
    }

    public Inventory getLeftSide() {
        return left;
    }

    public Inventory getRightSide() {
        return right;
    }

    @Override
    public void setContents(ItemStack[] items) {
        if (getInventory().getContents().length < items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getInventory().getContents().length + " or less");
        }
        ItemStack[] leftItems = new ItemStack[left.getSize()], rightItems = new ItemStack[right.getSize()];
        System.arraycopy(items, 0, leftItems, 0, Math.min(left.getSize(),items.length));
        left.setContents(leftItems);
        if (items.length >= left.getSize()) {
            System.arraycopy(items, left.getSize(), rightItems, 0, Math.min(right.getSize(), items.length - left.getSize()));
            right.setContents(rightItems);
        }
    }

    @Override
    public DoubleChest getHolder() {
        return new DoubleChest(this);
    }
}
