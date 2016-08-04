package com.teaminfinity.elementalinvocations.reference;

import com.teaminfinity.elementalinvocations.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class InventoryTabs {
    public static final CreativeTabs ELEMNTAL_INVOCATIONS = new CreativeTabs(Reference.MOD_ID) {
        @Override
        public Item getTabIconItem() {
            return ItemRegistry.getInstance().itemDebugger;
        }
    };
}
