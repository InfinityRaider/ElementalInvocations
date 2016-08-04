package com.teaminfinity.elementalinvocations.item;

import net.minecraft.item.crafting.IRecipe;

import java.util.List;

/**
 * Interface used to ease recipe registering
 */
public interface IItemWithRecipe extends IInfinityItem {
    /**
     * @return a list of all recipes to be registered
     */
    List<IRecipe> getRecipes();
}
