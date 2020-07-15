package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.inventory.DoubleInventory}
 */
public interface DoubleInventoryExtra {

    org.bukkit.inventory.InventoryHolder getOwner();

    void setMaxStackSize(int var0);

    net.minecraft.item.ItemStack[] getContents();

    java.util.List getViewers();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);
}
