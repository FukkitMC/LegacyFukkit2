package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.block.entity.ChestBlockEntity}
 */
public interface ChestBlockEntityExtra {

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getViewers();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    boolean F();

    net.minecraft.item.ItemStack[] getContents();

    void setMaxStackSize(int var0);
}
