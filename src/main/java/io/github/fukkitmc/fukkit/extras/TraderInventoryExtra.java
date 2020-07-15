package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.village.TraderInventory}
 */
public interface TraderInventoryExtra {

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getViewers();

    org.bukkit.inventory.InventoryHolder getOwner();

    net.minecraft.item.ItemStack[] getContents();

    void setMaxStackSize(int var0);

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);
}
