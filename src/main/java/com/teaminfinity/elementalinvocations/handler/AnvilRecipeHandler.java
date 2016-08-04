package com.teaminfinity.elementalinvocations.handler;

import com.teaminfinity.elementalinvocations.item.ItemElementalCore;
import com.teaminfinity.elementalinvocations.item.ItemWand;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AnvilRecipeHandler {
    private static final AnvilRecipeHandler INSTANCE = new AnvilRecipeHandler();

    public static AnvilRecipeHandler getInstance() {
        return INSTANCE;
    }

    private AnvilRecipeHandler() {}

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void applyAnvilRecipe(AnvilUpdateEvent event) {
        ItemStack wandStack = event.getLeft();
        ItemStack coreStack = event.getRight();
        if(wandStack == null || !(wandStack.getItem() instanceof ItemWand) || coreStack == null || !(coreStack.getItem() instanceof ItemElementalCore)) {
            return;
        }
        ItemWand wand = (ItemWand) wandStack.getItem();
        ItemElementalCore core = (ItemElementalCore) coreStack.getItem();
        ItemStack result = wand.applyElementalCore(wandStack, core.getElementalCore(coreStack));
        if(result.getItemDamage() != wandStack.getItemDamage()) {
            event.setMaterialCost(1);
            event.setCost(10 * (wand.getWandCore(result).tier() + 1));
            event.setOutput(result);
        }
    }
}
