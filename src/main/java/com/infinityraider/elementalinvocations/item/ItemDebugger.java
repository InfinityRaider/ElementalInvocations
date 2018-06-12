package com.infinityraider.elementalinvocations.item;

import com.google.common.collect.ImmutableList;
import com.infinityraider.elementalinvocations.utility.debug.*;
import com.infinityraider.infinitylib.item.IItemWithModel;
import com.infinityraider.infinitylib.item.ItemDebuggerBase;
import com.infinityraider.infinitylib.utility.debug.DebugMode;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.reference.InventoryTabs;
import com.infinityraider.elementalinvocations.reference.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.List;

public class ItemDebugger extends ItemDebuggerBase implements IItemWithModel {
    private static final List<DebugMode> MODES = ImmutableList.copyOf(createNewList());

    public ItemDebugger() {
        super(false);
        this.setCreativeTab(InventoryTabs.ELEMENTAL_INVOCATIONS);
    }

    @Override
    protected List<DebugMode> getDebugModes() {
        return MODES;
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        return ImmutableList.of(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":debugger", "inventory")));
    }

    private static final List<DebugMode> createNewList() {
        List<DebugMode> list = new ArrayList<>();
        list.add(new DebugModeClearMagicProperties());
        list.add(new DebugModeAddSoul());
        list.add(new DebugModeConfusion());
        list.add(new DebugModeDisplayMagicProperties());
        list.add(new DebugModeRenderInstability());
        for(Element element : Element.values()) {
            list.add(new DebugModeConfigureAdeptness(element));
        }
        return list;
    }
}
