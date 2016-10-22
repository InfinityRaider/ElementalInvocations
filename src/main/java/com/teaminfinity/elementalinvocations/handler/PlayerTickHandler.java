package com.teaminfinity.elementalinvocations.handler;

import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PlayerTickHandler {
    private static final PlayerTickHandler INSTANCE = new PlayerTickHandler();

    public static PlayerTickHandler getInstance() {
        return INSTANCE;
    }

    private PlayerTickHandler() {}

    /**
     * used to tick the player's capabilities
     */
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.START) {
            EntityPlayer player = event.player;
            IPlayerMagicProperties properties = CapabilityPlayerMagicProperties.getMagicProperties(player);
            if(properties != null) {
                properties.updateTick();
            }
        }
    }
}
