package io.github.fukkitmc.legacy.mixins.extra;


import io.github.fukkitmc.legacy.extra.PlayerInventoryExtra;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin implements PlayerInventoryExtra {
    @Override
    public ItemStack[] getArmorContents() {
        return new ItemStack[0];
    }
}
