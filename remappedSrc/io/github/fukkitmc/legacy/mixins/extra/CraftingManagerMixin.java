package io.github.fukkitmc.legacy.mixins.extra;


import io.github.fukkitmc.legacy.extra.CraftingManagerExtra;
import net.minecraft.recipe.Recipes;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Recipes.class)
public class CraftingManagerMixin implements CraftingManagerExtra {
    @Override
    public void sort() {

    }
}
