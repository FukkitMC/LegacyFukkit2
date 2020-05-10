package io.github.fukkitmc.legacy.mixins.craftbukkit;

import io.github.fukkitmc.legacy.extra.ContainerExtra;
import io.github.fukkitmc.legacy.extra.EntityExtra;
import net.minecraft.container.Container;
import net.minecraft.container.PlayerContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.*;
import org.bukkit.craftbukkit.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerContainer.class)
public abstract class ContainerPlayerMixin extends Container implements ContainerExtra {

    @Shadow public CraftingInventory craftInventory;

    @Shadow public Inventory resultInventory;

    @Shadow public CraftInventoryView bukkitEntity;

    @Shadow public PlayerInventory player;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(PlayerInventory playerInventory, boolean bl, PlayerEntity entityHuman, CallbackInfo ci){
        this.player = playerInventory;
    }

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryCrafting inventory = new CraftInventoryCrafting(this.craftInventory, this.resultInventory);
        bukkitEntity = new CraftInventoryView((HumanEntity) ((EntityExtra)this.player.player).getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end

}
