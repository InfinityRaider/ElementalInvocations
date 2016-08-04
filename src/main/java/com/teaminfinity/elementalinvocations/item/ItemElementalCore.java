package com.teaminfinity.elementalinvocations.item;

import com.teaminfinity.elementalinvocations.reference.InventoryTabs;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.Tuple;

import java.util.Collections;
import java.util.List;

public class ItemElementalCore extends ItemBase {
    public ItemElementalCore() {
        super("elemental_core");
        this.setCreativeTab(InventoryTabs.ELEMNTAL_INVOCATIONS);
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        return Collections.emptyList();
    }
}
