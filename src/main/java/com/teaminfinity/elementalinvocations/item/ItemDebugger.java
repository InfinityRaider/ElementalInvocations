package com.teaminfinity.elementalinvocations.item;

import com.google.common.collect.ImmutableList;
import com.teaminfinity.elementalinvocations.reference.InventoryTabs;
import com.teaminfinity.elementalinvocations.reference.Reference;
import com.teaminfinity.elementalinvocations.utility.debug.DebugMode;
import com.teaminfinity.elementalinvocations.utility.debug.DebugModeCapability;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemDebugger extends ItemDebuggerBase {
    public ItemDebugger() {
        super();
        this.setCreativeTab(InventoryTabs.ELEMNTAL_INVOCATIONS);
    }

    @Override
    protected List<DebugMode> getDebugModes() {
        List<DebugMode> list = new ArrayList<>();
        list.add(new DebugModeCapability());
        return list;
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        return ImmutableList.of(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":debugger", "inventory")));
    }
}
