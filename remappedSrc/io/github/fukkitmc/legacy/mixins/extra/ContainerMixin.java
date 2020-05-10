package io.github.fukkitmc.legacy.mixins.extra;

import io.github.fukkitmc.legacy.extra.ContainerExtra;
import io.github.fukkitmc.legacy.extra.IInventoryExtra;
import net.minecraft.container.Container;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.InventoryView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Container.class)
public class ContainerMixin implements ContainerExtra {

    @Override
    public void transferTo(Container other, org.bukkit.craftbukkit.entity.CraftHumanEntity player) {
        InventoryView source = this.getBukkitView(), destination = ((ContainerExtra)other).getBukkitView();
        ((IInventoryExtra)((CraftInventory) source.getTopInventory()).getInventory()).onClose(player);
        ((IInventoryExtra)((CraftInventory) source.getBottomInventory()).getInventory()).onClose(player);
        ((IInventoryExtra)((CraftInventory) destination.getTopInventory()).getInventory()).onOpen(player);
        ((IInventoryExtra)((CraftInventory) destination.getBottomInventory()).getInventory()).onOpen(player);
    }
}
