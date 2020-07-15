package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.entity.effect.HealthBoostStatusEffect}
 */
public interface HealthBoostStatusEffectExtra {

    void onOpen(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    void setMaxStackSize(int var0);

    java.util.List getViewers();

    net.minecraft.item.ItemStack[] getContents();

    org.bukkit.inventory.InventoryHolder getOwner();

    void onClose(org.bukkit.craftbukkit.entity.CraftHumanEntity var0);

    int getMaxStackSize();
}
