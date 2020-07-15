package org.bukkit.craftbukkit.inventory;

import java.util.Iterator;
import net.minecraft.recipe.Recipes;
import net.minecraft.recipes.SmeltingRecipes;
import org.bukkit.inventory.Recipe;

public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<net.minecraft.recipe.Recipe> recipes;
    private final Iterator<net.minecraft.item.ItemStack> smeltingCustom;
    private final Iterator<net.minecraft.item.ItemStack> smeltingVanilla;
    private Iterator<?> removeFrom = null;

    public RecipeIterator() {
        this.recipes = Recipes.getRecipes().method_70().iterator();
        this.smeltingCustom = SmeltingRecipes.getInstance().customRecipes.keySet().iterator();
        this.smeltingVanilla = SmeltingRecipes.getInstance().ORIGINAL_PRODUCT_MAP.keySet().iterator();
    }

    public boolean hasNext() {
        return recipes.hasNext() || smeltingCustom.hasNext() || smeltingVanilla.hasNext();
    }

    public Recipe next() {
        if (recipes.hasNext()) {
            removeFrom = recipes;
            return recipes.next().toBukkitRecipe();
        } else {
            net.minecraft.item.ItemStack item;
            if (smeltingCustom.hasNext()) {
                removeFrom = smeltingCustom;
                item = smeltingCustom.next();
            } else {
                removeFrom = smeltingVanilla;
                item = smeltingVanilla.next();
            }

            CraftItemStack stack = CraftItemStack.asCraftMirror(SmeltingRecipes.getInstance().method_55(item));

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
