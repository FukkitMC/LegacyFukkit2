package org.bukkit.craftbukkit.inventory;

import net.minecraft.village.TraderInventory;
import org.bukkit.inventory.MerchantInventory;

public class CraftInventoryMerchant extends CraftInventory implements MerchantInventory {
    public CraftInventoryMerchant(TraderInventory merchant) {
        super(merchant);
    }
}
