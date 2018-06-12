package com.infinityraider.elementalinvocations.item;

import com.google.common.collect.ImmutableList;
import com.infinityraider.elementalinvocations.config.ModConfiguration;
import com.infinityraider.elementalinvocations.magic.ElementalCore;
import com.infinityraider.infinitylib.item.ItemWithModelBase;
import com.infinityraider.infinitylib.utility.IRecipeRegister;
import com.infinityraider.infinitylib.utility.TranslationHelper;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.reference.InventoryTabs;
import com.infinityraider.elementalinvocations.reference.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemElementalCore extends ItemWithModelBase implements IRecipeRegister {
    public final List<ElementalCore> CORES;

    public ItemElementalCore() {
        super("elemental_core");
        this.CORES = ElementalCore.getCores();
        this.setCreativeTab(InventoryTabs.ELEMENTAL_INVOCATIONS);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
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
        return (core.element().getTextFormat() + "" + TranslationHelper.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + '_' + core.element().name().toLowerCase() + ".name")).trim();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        Element element = this.getElement(stack);
        tooltip.add(TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".core." + element.name().toLowerCase()));
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

    public Element getElement(ItemStack stack) {
        return getElementalCore(stack).element();
    }

    public List<IRecipe> getRecipes() {
        if(ModConfiguration.getInstance().enableOrbRecipes()) {
            return ImmutableList.of(
                    //fire
                    new ShapedOreRecipe(new ItemStack(this, 1, 0),
                            " f ", "bsb", " f ",
                            'f', new ItemStack(Items.FIRE_CHARGE),
                            'b', new ItemStack(Items.BLAZE_ROD),
                            's', new ItemStack(Items.NETHER_STAR)),
                    //water
                    new ShapedOreRecipe(new ItemStack(this, 1, 1),
                            " b ", "psp", " b ",
                            'b', new ItemStack(Items.WATER_BUCKET),
                            'p', new ItemStack(Items.PRISMARINE_CRYSTALS),
                            's', new ItemStack(Items.NETHER_STAR)),
                    //air
                    new ShapedOreRecipe(new ItemStack(this, 1, 2),
                            " f ", "gsg", " f ",
                            'f', new ItemStack(Items.FEATHER),
                            'g', new ItemStack(Items.GHAST_TEAR),
                            's', new ItemStack(Items.NETHER_STAR)),
                    //earth
                    new ShapedOreRecipe(new ItemStack(this, 1, 3),
                            " o ", "ese", " o ",
                            'o', new ItemStack(Blocks.OBSIDIAN),
                            'e', new ItemStack(Items.EMERALD),
                            's', new ItemStack(Items.NETHER_STAR)),
                    //death
                    new ShapedOreRecipe(new ItemStack(this, 1, 4),
                            " w ", "psp", " w ",
                            'w', new ItemStack(Items.SKULL, 1, 1),
                            'p', new ItemStack(Items.ENDER_PEARL),
                            's', new ItemStack(Items.NETHER_STAR)),
                    //life
                    new ShapedOreRecipe(new ItemStack(this, 1, 5),
                            " a ", "bsb", " a ",
                            'a', new ItemStack(Items.GOLDEN_APPLE),
                            'b', new ItemStack(Items.MILK_BUCKET),
                            's', new ItemStack(Items.NETHER_STAR))
            );
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void registerRecipes() {
        this.getRecipes().forEach(GameRegistry::addRecipe);
    }

    public static class DamageSourceChangeAffinity extends DamageSource {
        public DamageSourceChangeAffinity() {
            super("changeAffinity");
            this.setMagicDamage();
            this.setDamageBypassesArmor();
        }
    }
}
