package com.infinityraider.elementalinvocations;

import com.infinityraider.elementalinvocations.network.*;
import com.infinityraider.elementalinvocations.proxy.IProxy;
import com.infinityraider.elementalinvocations.registry.BlockRegistry;
import com.infinityraider.elementalinvocations.registry.EntityRegistry;
import com.infinityraider.elementalinvocations.registry.ItemRegistry;
import com.infinityraider.infinitylib.InfinityMod;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.elementalinvocations.reference.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        version = Reference.MOD_VERSION,
        dependencies = "required-after:infinitylib"
)
public class ElementalInvocations extends InfinityMod {
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
        wrapper.registerMessage(MessageInvoke.class);
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
}
