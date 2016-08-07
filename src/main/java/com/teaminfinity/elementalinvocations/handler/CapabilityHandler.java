package com.teaminfinity.elementalinvocations.handler;

import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProvider;
import com.teaminfinity.elementalinvocations.magic.spell.death.PlayerSoulCollectionProvider;
import com.teaminfinity.elementalinvocations.network.MessageRequestCapabilities;
import com.teaminfinity.elementalinvocations.network.MessageSyncCapabilities;
import com.teaminfinity.elementalinvocations.network.NetworkWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityHandler {
    private static final CapabilityHandler INSTANCE = new CapabilityHandler();

    public static CapabilityHandler getInstance() {
        return INSTANCE;
    }

    private CapabilityHandler() {}

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void addEntityCapabilities(AttachCapabilitiesEvent.Entity event) {
        if(event.getEntity() instanceof EntityPlayer) {
            event.addCapability(PlayerMagicProvider.KEY, new PlayerMagicProvider((EntityPlayer) event.getEntity()));
            event.addCapability(PlayerSoulCollectionProvider.KEY, new PlayerSoulCollectionProvider((EntityPlayer) event.getEntity()));
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        if((event.getEntity() instanceof EntityPlayer)) {
            if(event.getWorld().isRemote) {
                NetworkWrapper.getInstance().sendToServer(new MessageRequestCapabilities((EntityPlayer) event.getEntity()));
            } else {
                IPlayerMagicProperties properties = PlayerMagicProvider.getMagicProperties((EntityPlayer) event.getEntity());
                if(properties != null) {
                    NetworkWrapper.getInstance().sendToAll(new MessageSyncCapabilities((EntityPlayer) event.getEntity(), properties.writeToNBT()));
                }
            }
        }
    }
}
