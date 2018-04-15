package com.infinityraider.elementalinvocations.proxy;

import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import com.infinityraider.elementalinvocations.handler.*;
import com.infinityraider.infinitylib.modules.dualwield.ModuleDualWield;
import com.infinityraider.infinitylib.modules.playerstate.ModulePlayerState;
import com.infinityraider.infinitylib.modules.specialpotioneffect.ModuleSpecialPotion;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import com.infinityraider.elementalinvocations.magic.spell.SpellInitializer;
import com.infinityraider.elementalinvocations.potion.PotionConfusion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public interface IProxy extends IProxyBase {
    @Override
    default void initStart(FMLInitializationEvent event) {
        registerKeyBindings();
        SpellInitializer.init();
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
        this.registerEventHandler(HexHandler.getInstance());
        this.registerEventHandler(PlayerTickHandler.getInstance());
        this.registerEventHandler(DamageReductorHandler.getInstance());
        this.registerEventHandler(EnchantingHandler.getInstance());
        NetworkRegistry.INSTANCE.registerGuiHandler(ElementalInvocations.instance, EnchantingHandler.getInstance());
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
