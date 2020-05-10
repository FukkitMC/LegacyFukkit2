package io.github.fukkitmc.legacy.mixins.extra;


import io.github.fukkitmc.legacy.extra.IRecipeExtra;
import org.bukkit.inventory.Recipe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(net.minecraft.recipe.Recipe.class)
public interface IRecipeMixin extends IRecipeExtra {
    @Override
    default Recipe toBukkitRecipe() {
        return null;
    }
}
