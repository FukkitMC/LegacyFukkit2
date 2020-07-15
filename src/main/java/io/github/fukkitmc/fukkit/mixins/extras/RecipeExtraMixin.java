package io.github.fukkitmc.fukkit.mixins.extras;

import io.github.fukkitmc.fukkit.extras.RecipeExtra;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Mixin for extra {@link RecipeExtra}
 */
@Mixin(net.minecraft.recipe.Recipe.class)
public interface RecipeExtraMixin extends RecipeExtra {
}
