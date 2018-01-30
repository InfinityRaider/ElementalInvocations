package com.infinityraider.elementalinvocations.item;

import com.google.common.collect.ImmutableList;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import com.infinityraider.infinitylib.item.ItemWithModelBase;
import com.infinityraider.infinitylib.modules.dualwield.IDualWieldedWeapon;
import com.infinityraider.infinitylib.utility.IRecipeRegister;
import com.infinityraider.infinitylib.utility.TranslationHelper;
import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IMagicCharge;
import com.infinityraider.elementalinvocations.api.IPlayerMagicProperties;
import com.infinityraider.elementalinvocations.reference.Constants;
import com.infinityraider.elementalinvocations.reference.InventoryTabs;
import com.infinityraider.elementalinvocations.reference.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemWand extends ItemWithModelBase implements IRecipeRegister, IDualWieldedWeapon {
    public final List<WandCore> CORES;

    public ItemWand() {
        super("wand");
        this.CORES = WandCore.getCores();
        this.setHasSubtypes(true);
        this.setCreativeTab(InventoryTabs.ELEMENTAL_INVOCATIONS);
        this.setMaxStackSize(1);
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        List<Tuple<Integer, ModelResourceLocation>> list = new ArrayList<>();
        //wand without core
        list.add(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":wand", "inventory")));
        //wand with each core
        list.addAll(CORES.stream().map(core -> new Tuple<>(core.getMeta(), core.getModelResourceLocation())).collect(Collectors.toList()));
        return list;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("deprecation")
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        WandCore core = getWandCore(stack);
        if(core == null) {
            tooltip.add(TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".wand.noCore"));
        } else {
            tooltip.add(TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".wand.coreType") + ": " + core.element().getTextFormat() + core.element().name());
            tooltip.add(TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".wand.coreLevel") + ": " + (core.tier() + 1));
        }
        tooltip.add(" ");
        if(ElementalInvocations.proxy.isShiftKeyPressed()) {
            tooltip.add(TextFormatting.ITALIC + TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".wand.info.L1"));
            tooltip.add(TextFormatting.ITALIC + TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".wand.info.L2"));
            tooltip.add(TextFormatting.ITALIC + TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".wand.info.L3"));
            tooltip.add(TextFormatting.ITALIC + TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".wand.info.L4"));
            tooltip.add(" ");
            tooltip.add(TextFormatting.ITALIC + TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".wand.info.L5"));
            tooltip.add(TextFormatting.ITALIC + TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".wand.info.L6"));
            tooltip.add(TextFormatting.ITALIC + TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".wand.info.L7"));
        } else {
            tooltip.add(TextFormatting.ITALIC + TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".wand.info"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        //wand without a core
        subItems.add(new ItemStack(this, 1, 0));
        //all cores
        subItems.addAll(CORES.stream().map(core -> new ItemStack(this, 1, core.getMeta())).collect(Collectors.toList()));
    }

    @Nullable
    public WandCore getWandCore(ItemStack stack) {
        if(stack == null || stack.getItemDamage() > CORES.size() || stack.getItemDamage() <= 0) {
            return null;
        }
        return CORES.get(stack.getItemDamage() - 1);
    }

    public List<IRecipe> getRecipes() {
        return ImmutableList.of(
                new ShapedOreRecipe(this, "idi", " i ", " i ", 'i', "ingotIron", 'd', "gemDiamond")
        );
    }

    @Override
    public void registerRecipes() {
        this.getRecipes().forEach(GameRegistry::addRecipe);
    }

    @Override
    public void onItemUsed(ItemStack stack, EntityPlayer player, boolean shift, boolean ctrl, EnumHand hand) {
        IPlayerMagicProperties properties = CapabilityPlayerMagicProperties.getMagicProperties(player);
        if (properties == null) {
            return;
        }
        if (!player.worldObj.isRemote) {
            //conjure charge
            properties.addCharge(this.getCharge(stack));
        }
    }

    @Override
    public boolean onItemAttack(ItemStack stack, EntityPlayer player, Entity e, boolean shift, boolean ctrl, EnumHand hand) {
        //wands do not damage the entity, they are only used to invoke charges
        return false;
    }

    public IMagicCharge getCharge(ItemStack stack) {
        WandCore core = this.getWandCore(stack);
        return core == null ? null : core.getCharge();
    }

    public ItemStack applyElementalCore(ItemStack stack, ItemElementalCore.ElementalCore core) {
        WandCore wandCore = this.getWandCore(stack);
        if(wandCore == null) {
            return new ItemStack(this, 1, core.element().ordinal() * Constants.CORE_TIERS + 1);
        }
        if(wandCore.element() == core.element() && wandCore.tier() < (Constants.CORE_TIERS - 1)) {
            return new ItemStack(this, 1, core.element().ordinal() * Constants.CORE_TIERS + wandCore.tier() + 2);
        }
        return stack;
    }

    public static class WandCore {
        private final Element element;
        private final int tier;

        private WandCore(Element element, int tier) {
            this.element = element;
            this.tier = tier;
        }

        public Element element() {
            return element;
        }

        public int tier() {
            return tier;
        }

        public int getMeta() {
            return element().ordinal() * Constants.CORE_TIERS + tier() + 1;
        }

        public IMagicCharge getCharge() {
            return new IMagicCharge() {
                @Override
                public Element element() {
                    return element;
                }

                @Override
                public int level() {
                    return tier + 1;
                }
            };
        }

        @SideOnly(Side.CLIENT)
        public ModelResourceLocation getModelResourceLocation() {
            return new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":wand_" + element().name().toLowerCase() + "_" + tier(), "inventory");
        }

        public static List<ItemWand.WandCore> getCores() {
            List<ItemWand.WandCore> cores = new ArrayList<>();
            for(int i = 0; i < Element.values().length; i++) {
                for(int j = 0; j < Constants.CORE_TIERS; j++) {
                    cores.add(new ItemWand.WandCore(Element.values()[i], j));
                }
            }
            return ImmutableList.copyOf(cores);
        }
    }
}
