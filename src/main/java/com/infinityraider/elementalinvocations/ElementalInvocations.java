package com.infinityraider.elementalinvocations;

import com.infinityraider.elementalinvocations.api.ElementalInvocationsAPI;
import com.infinityraider.elementalinvocations.api.IElementalInvocationsAPI;
import com.infinityraider.elementalinvocations.api.IMagicDamageHandler;
import com.infinityraider.elementalinvocations.api.IPlayerMagicProperties;
import com.infinityraider.elementalinvocations.api.souls.ISoulCollection;
import com.infinityraider.elementalinvocations.api.spells.ISpellBuilder;
import com.infinityraider.elementalinvocations.api.spells.ISpellRegistry;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import com.infinityraider.elementalinvocations.magic.MagicDamageHandler;
import com.infinityraider.elementalinvocations.magic.spell.SpellBuilder;
import com.infinityraider.elementalinvocations.magic.spell.SpellRegistry;
import com.infinityraider.elementalinvocations.network.*;
import com.infinityraider.elementalinvocations.proxy.IProxy;
import com.infinityraider.elementalinvocations.registry.BlockRegistry;
import com.infinityraider.elementalinvocations.registry.EntityRegistry;
import com.infinityraider.elementalinvocations.registry.ItemRegistry;
import com.infinityraider.infinitylib.InfinityMod;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.elementalinvocations.reference.Reference;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        version = Reference.MOD_VERSION,
        dependencies = "required-after:infinitylib"
)
public class ElementalInvocations extends InfinityMod implements IElementalInvocationsAPI {
    @Mod.Instance(Reference.MOD_ID)
    @SuppressWarnings("unused")
    public static ElementalInvocations instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Override
    public IProxy proxy() {
        return proxy;
    }

    @Override
    public String getModId() {
        return Reference.MOD_ID;
    }

    @Override
    public Object getModBlockRegistry() {
        return BlockRegistry.getInstance();
    }

    @Override
    public Object getModItemRegistry() {
        return ItemRegistry.getInstance();
    }

    @Override
    public Object getModEntityRegistry() {
        return EntityRegistry.getInstance();
    }

    @Override
    public void registerMessages(INetworkWrapper wrapper) {
        wrapper.registerMessage(MessageAddCharge.class);
        wrapper.registerMessage(MessageChargeAction.class);
        wrapper.registerMessage(MessageKeyPressed.class);
        wrapper.registerMessage(MessageRenderSunstrike.class);
        wrapper.registerMessage(MessageSetPlayerPosition.class);
        wrapper.registerMessage(MessageStartStopBeam.class);
        wrapper.registerMessage(MessageStopChanneling.class);
        wrapper.registerMessage(MessageSwapPlayerPosition.class);
        wrapper.registerMessage(MessageSyncMagicProperties.class);
        wrapper.registerMessage(MessageSyncSouls.class);
        wrapper.registerMessage(MessageTrackPlayer.class);
        wrapper.registerMessage(MessageTrackPlayerUpdate.class);
        wrapper.registerMessage(MessageUpdateBeamRange.class);
    }

    @Override
    public Capability<? extends IPlayerMagicProperties> magicPropertiesCapability() {
        return CapabilityPlayerMagicProperties.PLAYER_MAGIC_PROPERTIES;
    }

    @Override
    public Capability<? extends ISoulCollection> soulCollectionCapability() {
        return CapabilityPlayerSoulCollection.PLAYER_SOUL_COLLECTION;
    }

    @Override
    public ISpellRegistry getSpellRegistry() {
        return SpellRegistry.getInstance();
    }

    @Override
    public ISpellBuilder getNewSpellBuilder() {
        return new SpellBuilder();
    }

    @Override
    public IMagicDamageHandler getDamageHandler() {
        return MagicDamageHandler.getInstance();
    }

    @Override
    public void initializeAPI() {
        Class<ElementalInvocationsAPI> clazz = ElementalInvocationsAPI.class;
        for(Field field : clazz.getDeclaredFields()) {
            if(Modifier.isStatic(field.getModifiers()) && field.getType().isAssignableFrom(IElementalInvocationsAPI.class)) {
                field.setAccessible(true);
                try {
                    field.set(this, null);
                } catch (IllegalAccessException e) {
                    this.getLogger().error("FAILED INITIALIZATION OF THE API");
                    this.getLogger().printStackTrace(e);
                }
            }
        }
    }
}
