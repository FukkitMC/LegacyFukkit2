package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.block.entity.BeaconBlockEntity}
 */
public interface BeaconBlockEntityExtra {

    void setMaxStackSize(int var0);

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getViewers();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    net.minecraft.item.ItemStack[] getContents();
}
