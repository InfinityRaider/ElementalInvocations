package com.infinityraider.elementalinvocations.reference;

import com.infinityraider.elementalinvocations.registry.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class InventoryTabs {
    public static final CreativeTabs ELEMENTAL_INVOCATIONS = new CreativeTabs(Reference.MOD_ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemRegistry.getInstance().itemDebugger);
        }
    };
}
