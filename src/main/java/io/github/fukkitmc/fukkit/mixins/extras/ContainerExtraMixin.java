package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.ContainerExtra;
import net.minecraft.container.Container;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.InventoryView;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link ContainerExtra}
 */
@Mixin(net.minecraft.container.Container.class)
public abstract class ContainerExtraMixin implements ContainerExtra {

    public void transferTo(Container other, org.bukkit.craftbukkit.entity.CraftHumanEntity player) {
        InventoryView source = this.getBukkitView(), destination = other.getBukkitView();
        ((CraftInventory) source.getTopInventory()).getInventory().onClose(player);
        ((CraftInventory) source.getBottomInventory()).getInventory().onClose(player);
        ((CraftInventory) destination.getTopInventory()).getInventory().onOpen(player);
        ((CraftInventory) destination.getBottomInventory()).getInventory().onOpen(player);
    }

}
