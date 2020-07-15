package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.vehicle.InventoryMinecartEntity}
 */
public interface InventoryMinecartEntityExtra {

    org.bukkit.inventory.InventoryHolder getOwner();

    net.minecraft.item.ItemStack[] getContents();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getViewers();

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    void setMaxStackSize(int var0);
}
