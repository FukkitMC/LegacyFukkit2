package org.bukkit.craftbukkit.inventory;

import net.minecraft.inventory.Inventory;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryHorse extends CraftInventory implements HorseInventory {

    public CraftInventoryHorse(Inventory inventory) {
        super(inventory);
    }

    public ItemStack getSaddle() {
        return getItem(0);
    }

    public ItemStack getArmor() {
       return getItem(1);
    }

    public void setSaddle(ItemStack stack) {
        setItem(0, stack);
    }

    public void setArmor(ItemStack stack) {
        setItem(1, stack);
    }
}
