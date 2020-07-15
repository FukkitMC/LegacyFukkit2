package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.block.entity.FurnaceBlockEntity}
 */
public interface FurnaceBlockEntityExtra {

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    void setMaxStackSize(int var0);

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getViewers();

    net.minecraft.item.ItemStack[] getContents();
}
