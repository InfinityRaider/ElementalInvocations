package com.teaminfinity.elementalinvocations;

import com.infinityraider.infinitylib.InfinityMod;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.teaminfinity.elementalinvocations.magic.spell.SpellInitializer;
import com.teaminfinity.elementalinvocations.network.*;
import com.teaminfinity.elementalinvocations.proxy.IProxy;
import com.teaminfinity.elementalinvocations.reference.Reference;
import com.teaminfinity.elementalinvocations.registry.BlockRegistry;
import com.teaminfinity.elementalinvocations.registry.EntityRegistry;
import com.teaminfinity.elementalinvocations.registry.ItemRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        version = Reference.MOD_VERSION,
        dependencies = "required-after:infinitylib"
)
public class ElementalInvocations extends InfinityMod {
    @Mod.Instance
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
        wrapper.registerMessage(MessageAttackDualWielded.class);
        wrapper.registerMessage(MessageInvoke.class);
        wrapper.registerMessage(MessageKeyPressed.class);
        wrapper.registerMessage(MessageMouseButtonPressed.class);
        wrapper.registerMessage(MessageRenderSunstrike.class);
        wrapper.registerMessage(MessageStopChanneling.class);
        wrapper.registerMessage(MessageSwingArm.class);
        wrapper.registerMessage(MessageSyncMagicProperties.class);
        wrapper.registerMessage(MessageSyncSouls.class);
        wrapper.registerMessage(MessageSyncState.class);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void preInitMod(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void initMod(FMLInitializationEvent event) {
        super.init(event);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void postInitMod(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        super.onServerAboutToStart(event);
    }
}
