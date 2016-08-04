package com.teaminfinity.elementalinvocations.item;

import com.google.common.collect.ImmutableList;
import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.reference.InventoryTabs;
import com.teaminfinity.elementalinvocations.reference.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemElementalCore extends ItemBase {
    public final List<ElementalCore> CORES;

    public ItemElementalCore() {
        super("elemental_core");
        this.CORES = ElementalCore.getCores();
        this.setCreativeTab(InventoryTabs.ELEMNTAL_INVOCATIONS);
        this.setHasSubtypes(true);
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        return CORES.stream().map(core
                -> new Tuple<>(core.getMeta(), core.getModelResourceLocation())).collect(Collectors.toList());
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        ElementalCore core = getElementalCore(stack);
        return (core.element().getTextFormat() + "" + I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + '_' + core.element().name().toLowerCase() + ".name")).trim();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".core.L1"));
        tooltip.add(I18n.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".core.L2"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        subItems.addAll(CORES.stream().map(core -> new ItemStack(this, 1, core.getMeta())).collect(Collectors.toList()));
    }

    public ElementalCore getElementalCore(ItemStack stack) {
        if(stack.getItemDamage() >= CORES.size() || stack.getItemDamage() < 0) {
            return CORES.get(0);
        }
        return CORES.get(stack.getItemDamage());
    }

    public static class ElementalCore {
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
            List<ElementalCore> cores = new ArrayList<>();
            for(int i = 0; i < Element.values().length; i++) {
                cores.add(new ElementalCore(Element.values()[i]));
            }
            return ImmutableList.copyOf(cores);
        }
    }
}
