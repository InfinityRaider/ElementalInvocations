package com.teaminfinity.elementalinvocations.proxy;

import com.infinityraider.infinitylib.modules.dualwield.ModuleDualWield;
import com.infinityraider.infinitylib.modules.playerstate.ModulePlayerState;
import com.infinityraider.infinitylib.modules.specialpotioneffect.ModuleSpecialPotion;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import com.teaminfinity.elementalinvocations.handler.*;
import com.teaminfinity.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.magic.spell.SpellInitializer;
import com.teaminfinity.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import com.teaminfinity.elementalinvocations.potion.PotionConfusion;
import com.teaminfinity.elementalinvocations.registry.PotionRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.event.*;

public interface IProxy extends IProxyBase {
    @Override
    default void initStart(FMLInitializationEvent event) {
        registerKeyBindings();
        SpellInitializer.init();
    }

    @Override
    default void postInitStart(FMLPostInitializationEvent event) {
        PotionRegistry.getInstance();
    }

    @Override
    default void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        this.registerEventHandler(LootHandler.getInstance());
    }


    @Override
    default void initConfiguration(FMLPreInitializationEvent event) {
        ConfigurationHandler.getInstance().init(event);
    }

    @Override
    default void activateRequiredModules() {
        ModuleDualWield.getInstance().activate();
        ModulePlayerState.getInstance().activate();
        ModuleSpecialPotion.getInstance().activate();
    }

    @Override
    default void registerEventHandlers() {
        this.registerEventHandler(AnvilRecipeHandler.getInstance());
        this.registerEventHandler(PlayerTickHandler.getInstance());
        this.registerEventHandler(SpellCastingHandler.getInstance());
        this.registerEventHandler(DamageReductorHandler.getInstance());
    }

    @Override
    default void registerCapabilities() {
        this.registerCapability(CapabilityPlayerMagicProperties.getInstance());
        this.registerCapability(CapabilityPlayerSoulCollection.getInstance());
    }

    default void registerKeyBindings() {}

    default boolean isShiftKeyPressed() {
        return false;
    }

    default void performConfusionEffect(PotionConfusion potion, EntityLivingBase entity, int amplification) {}
}
