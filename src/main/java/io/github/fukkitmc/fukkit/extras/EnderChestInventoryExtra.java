package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.inventory.EnderChestInventory}
 */
public interface EnderChestInventoryExtra {

    net.minecraft.item.ItemStack[] getContents();

    java.util.List getViewers();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    org.bukkit.inventory.InventoryHolder getOwner();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    int getMaxStackSize();

    void setMaxStackSize(int var0);
}
