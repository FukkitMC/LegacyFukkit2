package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.RecipesExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link RecipesExtra}
 */
@Mixin(net.minecraft.recipe.Recipes.class)
public interface RecipesExtraMixin extends RecipesExtra {
}
