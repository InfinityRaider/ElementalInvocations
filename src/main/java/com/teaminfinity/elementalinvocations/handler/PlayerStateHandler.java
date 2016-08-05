package com.teaminfinity.elementalinvocations.handler;

import com.teaminfinity.elementalinvocations.network.MessageSyncState;
import com.teaminfinity.elementalinvocations.network.NetworkWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.ThrowableImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.UUID;

public class PlayerStateHandler {
    private static final PlayerStateHandler INSTANCE = new PlayerStateHandler();

    public static PlayerStateHandler getInstance() {
        return INSTANCE;
    }

    private final HashMap<UUID, State> states;

    private PlayerStateHandler() {
        this.states = new HashMap<>();
    }

    public State getState(EntityPlayer player) {
        if(!states.containsKey(player.getUniqueID())) {
            states.put(player.getUniqueID(), new State(player));
        }
        return states.get(player.getUniqueID());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onEntityImpactEvent(ThrowableImpactEvent event) {
        if(event.getRayTraceResult().entityHit instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getRayTraceResult().entityHit;
            if(getState(player).isEthereal()) {
                event.setCanceled(true);
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onEntityHurtEvent(LivingHurtEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if(getState(player).isInvulnerable()) {
                event.setCanceled(true);
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    public void onPlayerRenderEvent(RenderPlayerEvent event) {
        if(getState(event.getEntityPlayer()).isInvisible()) {
            if(event.isCancelable()) {
                event.setCanceled(true);
            }
            event.setResult(Event.Result.DENY);
        }
    }

    public static class State {
        /** Player pointer */
        private final EntityPlayer player;

        /** Invisible to everything */
        private boolean invisible;
        /** Invulnerable to all damage */
        private boolean invulnerable;
        /** Non colliding with entities */
        private boolean ethereal;

        private State(EntityPlayer player) {
            this.player = player;
        }

        public EntityPlayer getPlayer() {
            return this.player;
        }

        public State setInvisible(boolean status) {
            if(status != this.invisible) {
                this.invisible = status;
                this.syncToClient();
            }
            return this;
        }

        public State setInvulnerable(boolean status) {
            if(status != this.invulnerable) {
                this.invulnerable = status;
                this.syncToClient();
            }
            return this;
        }

        public State setEthereal(boolean status) {
            if(status != this.ethereal) {
                this.ethereal = status;
                this.syncToClient();
            }
            return this;
        }

        public boolean isInvisible() {
            return this.invisible;
        }

        public boolean isInvulnerable() {
            return this.invulnerable;
        }

        public boolean isEthereal() {
            return this.ethereal;
        }

        private void syncToClient() {
            if(!getPlayer().getEntityWorld().isRemote) {
                NetworkWrapper.getInstance().sendToAll(new MessageSyncState(getPlayer(), this));
            }
        }
    }
}
