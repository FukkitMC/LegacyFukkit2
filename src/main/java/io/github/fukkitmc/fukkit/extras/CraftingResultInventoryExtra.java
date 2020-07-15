package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.inventory.CraftingResultInventory}
 */
public interface CraftingResultInventoryExtra {

    net.minecraft.item.ItemStack[] getContents();

    void setMaxStackSize(int var0);

    org.bukkit.inventory.InventoryHolder getOwner();

    java.util.List getViewers();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);
}
