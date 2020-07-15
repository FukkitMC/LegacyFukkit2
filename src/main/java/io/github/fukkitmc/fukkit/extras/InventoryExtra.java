package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.inventory.Inventory}
 */
public interface InventoryExtra {

    net.minecraft.item.ItemStack[] getContents();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getViewers();

    org.bukkit.inventory.InventoryHolder getOwner();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    void setMaxStackSize(int var0);
}
