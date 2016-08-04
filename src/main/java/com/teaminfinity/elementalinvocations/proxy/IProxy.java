package com.teaminfinity.elementalinvocations.proxy;

import com.teaminfinity.elementalinvocations.ModBase;
import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.handler.AnvilRecipeHandler;
import com.teaminfinity.elementalinvocations.handler.CapabilityHandler;
import com.teaminfinity.elementalinvocations.handler.ConfigurationHandler;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProperties;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProvider;
import com.teaminfinity.elementalinvocations.proxy.base.IProxyBase;
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
    }

    @Override
    default void postInitStart(FMLPostInitializationEvent event) {
        this.overridePlayerModel();
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
