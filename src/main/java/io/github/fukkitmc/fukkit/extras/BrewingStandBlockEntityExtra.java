package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.block.entity.BrewingStandBlockEntity}
 */
public interface BrewingStandBlockEntityExtra {

    void setMaxStackSize(int var0);

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    net.minecraft.item.ItemStack[] getContents();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    java.util.List getViewers();
}
