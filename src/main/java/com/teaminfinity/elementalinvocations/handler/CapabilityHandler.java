package com.teaminfinity.elementalinvocations.handler;

import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.magic.PlayerMagicProvider;
import com.teaminfinity.elementalinvocations.magic.spell.death.PlayerSoulCollectionProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CapabilityHandler {
    private static final CapabilityHandler INSTANCE = new CapabilityHandler();

    public static CapabilityHandler getInstance() {
        return INSTANCE;
    }

    private CapabilityHandler() {}

    /**
     * used to initialize player's capabilities
     */
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void addEntityCapabilities(AttachCapabilitiesEvent.Entity event) {
        if(event.getEntity() instanceof EntityPlayer) {
            event.addCapability(PlayerMagicProvider.KEY, new PlayerMagicProvider((EntityPlayer) event.getEntity()));
            event.addCapability(PlayerSoulCollectionProvider.KEY, new PlayerSoulCollectionProvider((EntityPlayer) event.getEntity()));
        }
    }

    /**
     * uused to tick the player's capabilities
     */
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.START) {
            EntityPlayer player = event.player;
            IPlayerMagicProperties properties = PlayerMagicProvider.getMagicProperties(player);
            if(properties != null) {
                properties.updateTick();
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerClone(PlayerEvent.Clone event) {
        EntityPlayer oldPlayer = event.getOriginal();
        EntityPlayer newPlayer = event.getEntityPlayer();
        if(!oldPlayer.getEntityWorld().isRemote) {
            IPlayerMagicProperties oldProps = PlayerMagicProvider.getMagicProperties(oldPlayer);
            IPlayerMagicProperties newProps = PlayerMagicProvider.getMagicProperties(newPlayer);
            if(oldPlayer != null && newProps != null) {
                newProps.readFromNBT(oldProps.writeToNBT());
            }
        }
    }
}
