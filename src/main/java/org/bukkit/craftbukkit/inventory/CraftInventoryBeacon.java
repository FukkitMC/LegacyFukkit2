package org.bukkit.craftbukkit.inventory;

import net.minecraft.block.entity.BeaconBlockEntity;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryBeacon extends CraftInventory implements BeaconInventory {
    public CraftInventoryBeacon(BeaconBlockEntity beacon) {
        super(beacon);
    }

    public void setItem(ItemStack item) {
        setItem(0, item);
    }

    public ItemStack getItem() {
        return getItem(0);
    }
}
