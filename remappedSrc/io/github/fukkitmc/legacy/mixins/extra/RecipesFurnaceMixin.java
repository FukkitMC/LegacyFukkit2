package io.github.fukkitmc.legacy.mixins.extra;


import com.google.common.collect.Maps;
import io.github.fukkitmc.legacy.extra.RecipesFurnaceExtra;
import net.minecraft.item.ItemStack;
import net.minecraft.server.RecipesFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RecipesFurnace.class)
public class RecipesFurnaceMixin implements RecipesFurnaceExtra {
    @Shadow public Map customRecipes;

    @Override
    public void registerRecipe(ItemStack itemstack, ItemStack itemstack1) {

    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(CallbackInfo ci){
        customRecipes = Maps.newHashMap();
    }
}
