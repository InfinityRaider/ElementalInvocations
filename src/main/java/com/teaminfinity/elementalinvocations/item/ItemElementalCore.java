package com.teaminfinity.elementalinvocations.item;

import com.google.common.collect.ImmutableList;
import com.infinityraider.infinitylib.item.ItemWithModelBase;
import com.infinityraider.infinitylib.utility.IRecipeRegister;
import com.infinityraider.infinitylib.utility.TranslationHelper;
import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.handler.ConfigurationHandler;
import com.teaminfinity.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.reference.Constants;
import com.teaminfinity.elementalinvocations.reference.InventoryTabs;
import com.teaminfinity.elementalinvocations.reference.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
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
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        boolean consume = false;
        if(entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            IPlayerMagicProperties properties = CapabilityPlayerMagicProperties.getMagicProperties(player);
            if(properties != null) {
                Element current = properties.getPlayerAffinity();
                Element orb = this.getElement(stack);
                if(current == null) {
                    properties.setPlayerAffinity(orb);
                    consume = true;
                } else if(current == orb && properties.getPlayerAdeptness() < Constants.MAX_LEVEL) {
                    if(ConfigurationHandler.getInstance().allowOrbLevelling) {
                        properties.setPlayerAdeptness(properties.getPlayerAdeptness() + 1);
                        consume = true;
                    }
                } else {
                    if(orb == current.getOpposite() && !world.isRemote) {
                        player.attackEntityFrom(new DamageSourceChangeAffinity(), properties.getPlayerAdeptness() * player.getMaxHealth() / Constants.MAX_LEVEL);
                    }
                    int lvl = properties.getPlayerAdeptness() - ConfigurationHandler.getInstance().levelLossOnElementChange;
                    properties.reset();
                    properties.setPlayerAffinity(orb);
                    properties.setPlayerAdeptness(lvl);
                    consume = true;
                }
            }
            if(player.capabilities.isCreativeMode) {
                consume = false;
            }
        }
        return consume ? null : stack;
    }


    public int getMaxItemUseDuration(ItemStack stack) {
        return 100;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
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
        tooltip.add(TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".core.L1"));
        IPlayerMagicProperties properties = CapabilityPlayerMagicProperties.getMagicProperties(player);
        Element orb = this.getElement(stack);
        if(properties == null) {
            tooltip.add(TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".core.neutral"));
        } else {
            if(orb == properties.getPlayerAffinity()) {
                tooltip.add(TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".core.positive"));
            } else if(orb.getOpposite() == properties.getPlayerAffinity()) {
                tooltip.add(TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".core.negative"));
            } else {
                tooltip.add(TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".core.neutral"));
            }
        }
        tooltip.add(TranslationHelper.translateToLocal(" "));
        tooltip.add(TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".core.L2"));
        if(properties != null && properties.getPlayerAffinity() == orb) {
            if(ConfigurationHandler.getInstance().allowOrbLevelling) {
                tooltip.add(TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".core.L3"));
            }
        } else {
            tooltip.add(TranslationHelper.translateToLocal("tooltip." + Reference.MOD_ID.toLowerCase() + ".core.L4") + " " + orb.getTextFormat() + orb.name());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
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
        if(ConfigurationHandler.getInstance().orbRecipes) {
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

    public static class DamageSourceChangeAffinity extends DamageSource {
        public DamageSourceChangeAffinity() {
            super("changeAffinity");
            this.setMagicDamage();
            this.setDamageBypassesArmor();
        }
    }
}
