package com.teaminfinity.elementalinvocations.item;

import com.google.common.collect.ImmutableList;
import com.infinityraider.infinitylib.item.ItemDebuggerBase;
import com.infinityraider.infinitylib.utility.debug.DebugMode;
import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.reference.InventoryTabs;
import com.teaminfinity.elementalinvocations.reference.Reference;
import com.teaminfinity.elementalinvocations.utility.debug.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.List;

public class ItemDebugger extends ItemDebuggerBase {
    public ItemDebugger() {
        super();
        this.setCreativeTab(InventoryTabs.ELEMNTAL_INVOCATIONS);
    }

    @Override
    protected List<DebugMode> getDebugModes() {
        List<DebugMode> list = new ArrayList<>();
        list.add(new DebugModeClearMagicProperties());
        for(Element element : Element.values()) {
            list.add(new DebugModeCycleAffinity(element));
        }
        list.add(new DebugModeAddSoul());
        list.add(new DebugModeConfusion());
        return list;
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        return ImmutableList.of(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":debugger", "inventory")));
    }
}
