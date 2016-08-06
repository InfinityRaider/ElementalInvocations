package com.teaminfinity.elementalinvocations.utility;

import com.teaminfinity.elementalinvocations.ModBase;
import com.teaminfinity.elementalinvocations.block.BlockBase;
import com.teaminfinity.elementalinvocations.entity.EntityRegistryEntry;
import com.teaminfinity.elementalinvocations.item.IInfinityItem;
import com.teaminfinity.elementalinvocations.item.IItemWithModel;
import com.teaminfinity.elementalinvocations.item.IItemWithRecipe;
import com.teaminfinity.elementalinvocations.network.NetworkWrapper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
        ReflectionHelper.forEachIn(mod.getModItemRegistry(), IInfinityItem.class, (IInfinityItem item) -> {
            if(item.isEnabled() && (item instanceof IItemWithRecipe)) {
                ((IItemWithRecipe) item).getRecipes().forEach(GameRegistry::addRecipe);
            }
        });
        LogHelper.debug("Finished Recipe Registration!");
    }

    public void registerEntities(ModBase mod) {
        LogHelper.debug("Starting Entity Registration...");
        ReflectionHelper.forEachIn(mod.getModEntityRegistry(), EntityRegistryEntry.class, (EntityRegistryEntry entry) -> {
            if(entry.isEnabled()) {
                entry.register(mod);
            }
        });
        LogHelper.debug("Finished Entity Registration!");
    }

    @SideOnly(Side.CLIENT)
    public void registerEntitiesClient(ModBase mod) {
        LogHelper.debug("Starting Entity Registration...");
        ReflectionHelper.forEachIn(mod.getModEntityRegistry(), EntityRegistryEntry.class, (EntityRegistryEntry entry) -> {
            if(entry.isEnabled()) {
                entry.registerClient(mod);
            }
        });
        LogHelper.debug("Finished Entity Registration!");
    }

    @SideOnly(Side.CLIENT)
    public void initRenderers(ModBase mod) {
        LogHelper.debug("Starting Renderer Registration...");
        //items
        ReflectionHelper.forEachIn(mod.getModItemRegistry(), IInfinityItem.class, (IInfinityItem item) -> {
            if ((item instanceof Item) && item.isEnabled()) {
                if(item instanceof IItemWithModel) {
                    for (Tuple<Integer, ModelResourceLocation> entry : ((IItemWithModel) item).getModelDefinitions()) {
                        ModelLoader.setCustomModelResourceLocation((Item) item, entry.getFirst(), entry.getSecond());
                    }
                }
            }
        });
        LogHelper.debug("Finished Renderer Registration!");
    }
}
