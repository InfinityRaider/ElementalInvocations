package com.infinityraider.elementalinvocations.registry;

import com.infinityraider.elementalinvocations.item.ItemWand;
import com.infinityraider.elementalinvocations.item.ItemDebugger;
import com.infinityraider.elementalinvocations.item.ItemElementalCore;
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
