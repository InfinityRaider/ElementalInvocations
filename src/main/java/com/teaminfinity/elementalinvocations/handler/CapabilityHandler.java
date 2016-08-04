package com.teaminfinity.elementalinvocations.handler;

import com.teaminfinity.elementalinvocations.magic.PlayerMagicProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
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
            event.addCapability(PlayerMagicProvider.KEY, new PlayerMagicProvider());
        }
    }
}
