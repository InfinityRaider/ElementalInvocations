package com.teaminfinity.elementalinvocations;

import com.teaminfinity.elementalinvocations.network.*;
import com.teaminfinity.elementalinvocations.proxy.IProxy;
import com.teaminfinity.elementalinvocations.proxy.base.IProxyBase;
import com.teaminfinity.elementalinvocations.reference.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        version = Reference.MOD_VERSION
)
public class ElementalInvocations extends ModBase {
    @Mod.Instance
    @SuppressWarnings("unused")
    public static ElementalInvocations instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Override
    public IProxyBase proxy() {
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
        wrapper.registerMessage(MessageMouseButtonPressed.class);
        wrapper.registerMessage(MessageSwingArm.class);
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
		SpellInitializer.init();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void postInitMod(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
