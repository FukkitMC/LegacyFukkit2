package org.bukkit.craftbukkit.inventory;

import io.github.fukkitmc.legacy.extra.IInventoryExtra;
import net.minecraft.inventory.Inventory;
import org.bukkit.block.BrewingStand;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryBrewer extends CraftInventory implements BrewerInventory {
    public CraftInventoryBrewer(Inventory inventory) {
        super(inventory);
    }

    public ItemStack getIngredient() {
        return getItem(3);
    }

    public void setIngredient(ItemStack ingredient) {
        setItem(3, ingredient);
    }

    @Override
    public BrewingStand getHolder() {
        return (BrewingStand) ((IInventoryExtra)inventory).getOwner();
    }
}
