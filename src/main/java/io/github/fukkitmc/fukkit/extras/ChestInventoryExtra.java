package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.container.ChestInventory}
 */
public interface ChestInventoryExtra extends ContainerExtra{

    org.bukkit.craftbukkit.inventory.CraftInventoryView getBukkitView();
}
