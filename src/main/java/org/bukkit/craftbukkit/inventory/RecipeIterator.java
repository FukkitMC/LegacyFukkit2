package org.bukkit.craftbukkit.inventory;

import java.util.Iterator;

import io.github.fukkitmc.legacy.extra.IRecipeExtra;
import org.bukkit.inventory.Recipe;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipes;
import net.minecraft.server.RecipesFurnace;

public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<net.minecraft.recipe.Recipe> recipes;
    private final Iterator smeltingCustom;
    private final Iterator<net.minecraft.item.ItemStack> smeltingVanilla;
    private Iterator<?> removeFrom = null;

    public RecipeIterator() {
        this.recipes = Recipes.getRecipes().getRecipes().iterator();
        this.smeltingCustom = RecipesFurnace.getInstance().customRecipes.keySet().iterator();
        this.smeltingVanilla = RecipesFurnace.getInstance().recipes.keySet().iterator();
    }

    public boolean hasNext() {
        return recipes.hasNext() || smeltingCustom.hasNext() || smeltingVanilla.hasNext();
    }

    public Recipe next() {
        if (recipes.hasNext()) {
            removeFrom = recipes;
            return ((IRecipeExtra)recipes.next()).toBukkitRecipe();
        } else {
            net.minecraft.item.ItemStack item;
            if (smeltingCustom.hasNext()) {
                removeFrom = smeltingCustom;
                item = (ItemStack) smeltingCustom.next();
            } else {
                removeFrom = smeltingVanilla;
                item = smeltingVanilla.next();
            }

            CraftItemStack stack = CraftItemStack.asCraftMirror(RecipesFurnace.getInstance().getResult(item));

            return new CraftFurnaceRecipe(stack, CraftItemStack.asCraftMirror(item));
        }
    }

    public void remove() {
        if (removeFrom == null) {
            throw new IllegalStateException();
        }
        removeFrom.remove();
    }
}
