package com.teaminfinity.elementalinvocations;

import com.teaminfinity.elementalinvocations.item.ItemDebugger;
import com.teaminfinity.elementalinvocations.item.ItemElementalCore;
import com.teaminfinity.elementalinvocations.item.ItemWand;
import net.minecraft.item.Item;

public class ItemRegistry {
    private static final ItemRegistry INSTANCE = new ItemRegistry();

    public static ItemRegistry getInstance() {
        return INSTANCE;
    }

    private ItemRegistry() {
        this.itemWand = new ItemWand();
        this.itemElementalCore = new ItemElementalCore();
        this.itemDebugger = new ItemDebugger();
    }

    public final Item itemWand;
    public final Item itemElementalCore;
    public final Item itemDebugger;
}
