package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.inventory.BasicInventory}
 */
public interface BasicInventoryExtra {

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    org.bukkit.inventory.InventoryHolder getOwner();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    void setMaxStackSize(int var0);

    java.util.List getViewers();

    net.minecraft.item.ItemStack[] getContents();
}
