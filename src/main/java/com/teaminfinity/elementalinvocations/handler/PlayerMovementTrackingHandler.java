package com.teaminfinity.elementalinvocations.handler;

import com.infinityraider.infinitylib.network.NetworkWrapper;
import com.teaminfinity.elementalinvocations.ElementalInvocations;
import com.teaminfinity.elementalinvocations.network.MessageTrackPlayerUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlayerMovementTrackingHandler {
    private static final PlayerMovementTrackingHandler INSTANCE = new PlayerMovementTrackingHandler();

    public static PlayerMovementTrackingHandler getInstance() {
        return INSTANCE;
    }

    private boolean isTracking;

    private PlayerMovementTrackingHandler() {}

    public void startTracking() {
        this.isTracking = true;
    }

    public void stopTracking() {
        this.isTracking = false;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(isTracking && event.phase == TickEvent.Phase.END && event.player == ElementalInvocations.proxy.getClientPlayer()) {
            EntityPlayer player = event.player;
            double deltaX = player.posX - player.prevPosX;
            double deltaY = player.posY - player.prevPosY;
            double deltaZ = player.posZ - player.prevPosZ;
            if(deltaX == 0 && deltaY == 0 && deltaZ == 0) {
                return;
            }
            NetworkWrapper.getInstance().sendToServer(new MessageTrackPlayerUpdate(deltaX, deltaY, deltaZ));
        }
    }
}
