package com.teaminfinity.elementalinvocations.proxy;

import com.infinityraider.infinitylib.modules.dualwield.ModuleDualWield;
import com.infinityraider.infinitylib.modules.playerstate.ModulePlayerState;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.handler.*;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProperties;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProvider;
import com.teaminfinity.elementalinvocations.magic.spell.SpellInitializer;
import com.teaminfinity.elementalinvocations.magic.spell.death.PlayerSoulCollection;
import com.teaminfinity.elementalinvocations.magic.spell.death.PlayerSoulCollectionProvider;
import com.teaminfinity.elementalinvocations.potion.PotionConfusion;
import com.teaminfinity.elementalinvocations.registry.PotionRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.*;
import com.teaminfinity.elementalinvocations.api.souls.ISoulCollection;

public interface IProxy extends IProxyBase {
    @Override
    default void preInitStart(FMLPreInitializationEvent event) {
        ConfigurationHandler.getInstance().init(event);
    }

    @Override
    default void preInitEnd(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(IPlayerMagicProperties.class, new PlayerMagicProvider.Storage(), PlayerMagicProperties.class);
        CapabilityManager.INSTANCE.register(ISoulCollection.class, new PlayerSoulCollectionProvider.Storage(), PlayerSoulCollection.class);
    }

    @Override
    default void initStart(FMLInitializationEvent event) {
        registerKeyBindings();
        SpellInitializer.init();
    }

    @Override
    default void postInitStart(FMLPostInitializationEvent event) {
        PotionRegistry.getInstance();
        this.overridePlayerModel();
    }

    @Override
    default void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        this.registerEventHandler(LootHandler.getInstance());
    }

    default void overridePlayerModel() {}

    @Override
    default void registerEventHandlers() {
        this.registerEventHandler(AnvilRecipeHandler.getInstance());
        this.registerEventHandler(CapabilityHandler.getInstance());
        this.registerEventHandler(SpellCastingHandler.getInstance());
        this.registerEventHandler(DamageReductorHandler.getInstance());
    }

    @Override
    default void activateRequiredModules() {
        ModulePlayerState.getInstance().activate();
        ModuleDualWield.getInstance().activate();
    }

    default void registerKeyBindings() {}

    default boolean isShiftKeyPressed() {
        return false;
    }

    default void performConfusionEffect(PotionConfusion potion, EntityLivingBase entity, int amplification) {}
}
