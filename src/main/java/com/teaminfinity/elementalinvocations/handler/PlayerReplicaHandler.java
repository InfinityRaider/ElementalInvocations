package com.teaminfinity.elementalinvocations.handler;

import com.teaminfinity.elementalinvocations.entity.EntityReplicate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

public class PlayerReplicaHandler {
    private static final PlayerReplicaHandler INSTANCE = new PlayerReplicaHandler();

    public static PlayerReplicaHandler getInstance() {
        return INSTANCE;
    }

    private final Map<EntityPlayer, List<EntityReplicate>> replicas;

    private PlayerReplicaHandler() {
        this.replicas = new IdentityHashMap<>();
    }

    public List<EntityReplicate> getReplicas(EntityPlayer player) {
        if(replicas.containsKey(player)) {
            return replicas.get(player);
        } else {
            return Collections.emptyList();
        }
    }

    public void addReplica(EntityReplicate replica) {
        if(!this.replicas.containsKey(replica.getPlayer())) {
            this.replicas.put(replica.getPlayer(), new ArrayList<>());
        }
        this.replicas.get(replica.getPlayer()).add(replica);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {

    }
}
