package com.infinityraider.elementalinvocations.magic;

import com.google.common.collect.ImmutableList;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.reference.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ElementalCore {
    private static final List<ElementalCore> CORES = createCores();
    private final Element element;

    private ElementalCore(Element element) {
        this.element = element;
    }

    public Element element() {
        return element;
    }

    public int getMeta() {
        return element().ordinal();
    }

    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getModelResourceLocation() {
        return new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":elemental_core_" + element().name().toLowerCase(), "inventory");
    }

    public static List<ElementalCore> getCores() {
        return CORES;
    }

    private static List<ElementalCore> createCores() {
        List<ElementalCore> cores = new ArrayList<>();
        for(int i = 0; i < Element.values().length; i++) {
            cores.add(new ElementalCore(Element.values()[i]));
        }
        return ImmutableList.copyOf(cores);
    }
}