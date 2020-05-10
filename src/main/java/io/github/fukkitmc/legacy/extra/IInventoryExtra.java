package io.github.fukkitmc.legacy.extra;

import net.minecraft.item.ItemStack;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;

public interface IInventoryExtra {

    ItemStack[] getContents();

    void onOpen(CraftHumanEntity who);

    void onClose(CraftHumanEntity who);

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    org.bukkit.inventory.InventoryHolder getOwner();

    void setMaxStackSize(int size);

    int MAX_STACK = 64;

    String getName();

}
