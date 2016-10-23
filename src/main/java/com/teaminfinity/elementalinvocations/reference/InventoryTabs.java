package com.teaminfinity.elementalinvocations.reference;

import com.teaminfinity.elementalinvocations.registry.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class InventoryTabs {
    public static final CreativeTabs ELEMENTAL_INVOCATIONS = new CreativeTabs(Reference.MOD_ID) {
        @Override
        public Item getTabIconItem() {
            return ItemRegistry.getInstance().itemDebugger;
        }
    };
}
