package com.teaminfinity.elementalinvocations.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public abstract class ItemBase extends Item implements IItemWithModel {
    private final String internalName;

    public ItemBase(String name) {
        super();
        this.internalName = name;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public String getInternalName() {
        return internalName;
    }

    public abstract List<String> getOreTags();
}