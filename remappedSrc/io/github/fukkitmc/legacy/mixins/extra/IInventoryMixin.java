package io.github.fukkitmc.legacy.mixins.extra;


import io.github.fukkitmc.legacy.extra.IInventoryExtra;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

@Mixin(Inventory.class)
public interface IInventoryMixin extends IInventoryExtra {
    @Override
    default ItemStack[] getContents() {
        return new ItemStack[0];
    }

    @Override
    default void onOpen(CraftHumanEntity who) {

    }

    @Override
    default void onClose(CraftHumanEntity who) {

    }

    @Override
    default List<HumanEntity> getViewers() {
        return null;
    }

    @Override
    default InventoryHolder getOwner() {
        return null;
    }

    @Override
    default void setMaxStackSize(int size) {

    }
}
