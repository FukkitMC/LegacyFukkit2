package io.github.fukkitmc.fukkit.mixins.net.minecraft.container;

import io.github.fukkitmc.fukkit.extras.ChestInventoryExtra;
import net.minecraft.container.ChestInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestInventory.class)
public class ChestInventoryMixin implements ChestInventoryExtra {

    @Shadow
    public Inventory inventory;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void constructor(Inventory inventory, Inventory playerInventory, PlayerEntity player, CallbackInfo ci){
        // Fukkit start - Check for instance of PlayerInventory & Save player
        if(inventory instanceof PlayerInventory){
            ((ChestInventory) (Object) this).playerInv = (PlayerInventory) inventory;
        }
        // Fukkit end
    }

    @Override
    public CraftInventoryView getBukkitView() {
        if (((ChestInventory) (Object) this).bukkitEntity != null) {
            return ((ChestInventory) (Object) this).bukkitEntity;
        }

        CraftInventory inventory;
        if (this.inventory instanceof PlayerInventory) {
            inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryPlayer((PlayerInventory) this.inventory);
        } else if (this.inventory instanceof DoubleInventory) {
            inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest((DoubleInventory) this.inventory);
        } else {
            inventory = new CraftInventory(this.inventory);
        }

        ((ChestInventory) (Object) this).bukkitEntity = new CraftInventoryView(((ChestInventory) (Object) this).playerInv.player.getBukkitEntity(), inventory, ((ChestInventory) (Object) this));
        return ((ChestInventory) (Object) this).bukkitEntity;
    }
}
