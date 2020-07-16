package io.github.fukkitmc.fukkit.extras;

/**
 * Extra for {@link net.minecraft.container.Container}
 */
public interface ContainerExtra {

    default void transferTo(net.minecraft.container.Container var0, org.bukkit.craftbukkit.entity.CraftHumanEntity var1){
        throw new RuntimeException("Transferto not implemented in " + this.getClass().getSimpleName());
    }

    org.bukkit.inventory.InventoryView getBukkitView();
}
