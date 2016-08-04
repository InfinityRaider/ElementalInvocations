package com.teaminfinity.elementalinvocations.proxy;

import com.teaminfinity.elementalinvocations.ModBase;
import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.api.spells.IPlayerSoulCollection;
import com.teaminfinity.elementalinvocations.handler.AnvilRecipeHandler;
import com.teaminfinity.elementalinvocations.handler.CapabilityHandler;
import com.teaminfinity.elementalinvocations.handler.ConfigurationHandler;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProperties;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProvider;
import com.teaminfinity.elementalinvocations.magic.spell.death.PlayerSoulCollection;
import com.teaminfinity.elementalinvocations.magic.spell.death.PlayerSoulCollectionProvider;
import com.teaminfinity.elementalinvocations.proxy.base.IProxyBase;
import com.teaminfinity.elementalinvocations.utility.ModHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy extends IProxyBase {
    @Override
    default void preInitStart(FMLPreInitializationEvent event) {
        ConfigurationHandler.getInstance().init(event);
        registerEventHandlers();
    }

    @Override
    default void preInitEnd(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(IPlayerMagicProperties.class, new PlayerMagicProvider.Storage(), PlayerMagicProperties.class);
        CapabilityManager.INSTANCE.register(IPlayerSoulCollection.class, new PlayerSoulCollectionProvider.Storage(), PlayerSoulCollection.class);
    }

    @Override
    default void postInitStart(FMLPostInitializationEvent event) {
        this.overridePlayerModel();
    }

    default void registerEntities(ModBase mod) {
        ModHelper.getInstance().registerEntities(mod);
    }

    default void overridePlayerModel() {}

    default void registerEventHandlers() {
        MinecraftForge.EVENT_BUS.register(AnvilRecipeHandler.getInstance());
        MinecraftForge.EVENT_BUS.register(CapabilityHandler.getInstance());
    }

    default void registerRenderers(ModBase mod) {}

    default boolean isShiftKeyPressed() {
        return false;
    }
}
