package com.teaminfinity.elementalinvocations.utility;

import com.teaminfinity.elementalinvocations.ModBase;
import com.teaminfinity.elementalinvocations.block.BlockBase;
import com.teaminfinity.elementalinvocations.item.IInfinityItem;
import com.teaminfinity.elementalinvocations.item.IItemWithRecipe;
import com.teaminfinity.elementalinvocations.item.ItemBase;
import com.teaminfinity.elementalinvocations.network.NetworkWrapper;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ModHelper {
    private static final ModHelper INSTANCE = new ModHelper();

    public static ModHelper getInstance() {
        return INSTANCE;
    }

    private ModHelper() {}

    public void RegisterBlocksAndItems(ModBase mod) {
        //blocks
        LogHelper.debug("Starting Block Registration...");
        ReflectionHelper.forEachIn(mod.getModBlockRegistry(), BlockBase.class, (BlockBase block) -> {
            if(block.isEnabled()) {
                LogHelper.debug("Registering Block: " + block.getInternalName());
                RegisterHelper.registerBlock(block, mod.getModId(), block.getInternalName(), block.getItemBlockClass());
                for(String tag: block.getOreTags()) {
                    OreDictionary.registerOre(tag, block);
                }
            }
        });
        LogHelper.debug("Finished Block Registration!");

        //items
        LogHelper.debug("Starting Item Registration...");
        ReflectionHelper.forEachIn(mod.getModItemRegistry(), IInfinityItem.class, (IInfinityItem item) -> {
            if((item instanceof Item) && item.isEnabled()) {
                LogHelper.debug("Registering Item: " + item.getInternalName());
                RegisterHelper.registerItem((Item) item, mod.getModId(), item.getInternalName());
                for(String tag: item.getOreTags()) {
                    OreDictionary.registerOre(tag, (Item) item);
                }
            }
        });
        LogHelper.debug("Finished Item Registration!");

        //network messages
        mod.registerMessages(NetworkWrapper.getInstance());
    }

    public void registerRecipes(ModBase mod) {
        LogHelper.debug("Starting Recipe Registration...");
        //blocks
        ReflectionHelper.forEachIn(mod.getModBlockRegistry(), BlockBase.class, (BlockBase block) -> {
            if(block.isEnabled() && (block instanceof IItemWithRecipe)) {
                ((IItemWithRecipe) block).getRecipes().forEach(GameRegistry::addRecipe);
            }
        });
        //items
        ReflectionHelper.forEachIn(mod.getModBlockRegistry(), ItemBase.class, (ItemBase item) -> {
            if(item.isEnabled() && (item instanceof IItemWithRecipe)) {
                ((IItemWithRecipe) item).getRecipes().forEach(GameRegistry::addRecipe);
            }
        });
        LogHelper.debug("Finished Recipe Registration!");
    }
}
