package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.inventory.CraftingInventory}
 */
public interface CraftingInventoryExtra {

    java.util.List getViewers();

    org.bukkit.inventory.InventoryHolder getOwner();

    net.minecraft.item.ItemStack[] getContents();

    org.bukkit.event.inventory.InventoryType getInvType();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    void setMaxStackSize(int var0);
}
