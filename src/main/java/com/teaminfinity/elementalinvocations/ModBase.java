package com.teaminfinity.elementalinvocations;

import com.teaminfinity.elementalinvocations.network.INetworkWrapper;
import com.teaminfinity.elementalinvocations.proxy.base.IProxyBase;
import com.teaminfinity.elementalinvocations.utility.LogHelper;
import com.teaminfinity.elementalinvocations.utility.ModHelper;
import net.minecraftforge.fml.common.event.*;

public abstract class ModBase {
    /**
     * @return The sided proxy object for this mod
     */
    public abstract IProxyBase proxy();

    /**
     * @return The mod ID of the mod
     */
    public abstract String getModId();

    /**
     * Used to register the Blocks, recipes, renderers, TileEntities, etc. for all the mod's blocks.
     * The object returned by this should have a field for each of its blocks
     * @return Block registry object or class
     */
    public abstract Object getModBlockRegistry();

    /**
     * Used to register the Items, recipes, renderers, etc. for all the mod's items.
     * The object returned by this should have a field for each of its items
     * @return Item registry object or class
     */
    public abstract Object getModItemRegistry();

    /**
     * Register all messages added by this mod
     * @param wrapper NetworkWrapper instance to register messages to
     */
    public abstract void registerMessages(INetworkWrapper wrapper);

    public final void preInit(FMLPreInitializationEvent event) {
        LogHelper.debug("Starting Pre-Initialization");
        proxy().preInitStart(event);
        ModHelper.getInstance().RegisterBlocksAndItems(this);
        ElementalInvocations.proxy.registerRenderers(this);
        proxy().preInitEnd(event);
        LogHelper.debug("Pre-Initialization Complete");
    }

    public final void init(FMLInitializationEvent event) {
        LogHelper.debug("Starting Initialization");
        proxy().initStart(event);
        ModHelper.getInstance().registerRecipes(this);
        proxy().initEnd(event);
        LogHelper.debug("Initialization Complete");
    }

    public final void postInit(FMLPostInitializationEvent event) {
        LogHelper.debug("Starting Post-Initialization");
        proxy().postInitStart(event);
        proxy().postInitEnd(event);
        LogHelper.debug("Post-Initialization Complete");
    }

    public final void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        proxy().onServerAboutToStart(event);
    }

    public final void onServerStarting(FMLServerStartingEvent event) {
        proxy().onServerStarting(event);
    }

    public final void onServerStarted(FMLServerStartedEvent event) {
        proxy().onServerStarted(event);
    }

    public final void onServerStopping(FMLServerStoppingEvent event) {
        proxy().onServerStopping(event);
    }

    public final void onServerStopped(FMLServerStoppedEvent event) {
        proxy().onServerStopped(event);
    }
}
