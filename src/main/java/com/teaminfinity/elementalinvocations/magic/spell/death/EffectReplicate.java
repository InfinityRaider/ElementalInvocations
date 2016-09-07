package com.teaminfinity.elementalinvocations.magic.spell.death;

import com.infinityraider.infinitylib.utility.RayTraceHelper;
import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.souls.ISoulCollection;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.entity.EntityReplicate;
import com.teaminfinity.elementalinvocations.utility.PlayerHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

import java.util.*;

public class EffectReplicate implements ISpellEffect {
    private static final int REPLICA_LIFETIME_PER_LEVEL = 10;

    private Map<UUID, List<EntityReplicate>> replicas = new HashMap<>();

    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        int potencyDeath = potencies[Element.DEATH.ordinal()];
        int potencyAir = potencies[Element.AIR.ordinal()];
        ISoulCollection collection = PlayerSoulCollectionProvider.getSoulCollection(caster);
        int amount = Math.min(potencyDeath/3, collection.getSoulCount());
        if(amount <= 0) {
            return false;
        }
        double x = caster.posX;
        double y = caster.posY;
        double z = caster.posZ;
        float yaw = caster.rotationYaw;
        double radius = 1.5F;
        List<EntityReplicate> replicas = new ArrayList<>();
        for(int i = 0; i < (amount + 1); i++) {
            double angle = (yaw + (i*360/(amount + 1)) + offsetForIndex(i)) % 360;
            double newX = x + radius*Math.cos(Math.toRadians(angle));
            double newZ = z + radius*Math.sin(Math.toRadians(angle));
            EntityLivingBase entity;
            if(i == 0) {
                entity = caster;
                PlayerHelper.setPlayerPosition(caster, newX, y, newZ);
            } else {
                replicas.add(new EntityReplicate(caster, this, potencyAir *20 * REPLICA_LIFETIME_PER_LEVEL / 2));
                entity = replicas.get(i - 1);
                collection.removeSoul();
                caster.getEntityWorld().spawnEntityInWorld(entity);
            }
            entity.setPosition(newX, y, newZ);
        }
        int swap = caster.getRNG().nextInt(replicas.size() + 1);
        if(swap < replicas.size()) {
            replicas.get(swap).swapWithPlayer();
        }
        if(!this.replicas.containsKey(caster.getUniqueID())) {
            this.replicas.put(caster.getUniqueID(), replicas);
        } else {
            this.replicas.get(caster.getUniqueID()).addAll(replicas);
        }
        return false;
    }

    protected int offsetForIndex(int index) {
        switch(index) {
            case 2: return 90;
            case 4: return 45;
            case 6: return 30;
            default: return 0;
        }
    }

    public boolean isLingeringEffect() {
        return true;
    }

    /**
     * Called every tick for lingering effects
     * @return true to end this effect
     */
    public boolean lingerUpdate(EntityPlayer caster) {
        return this.replicas.containsKey(caster.getUniqueID()) && this.replicas.get(caster.getUniqueID()).isEmpty();
    }

    /**
     * Called when the caster presses the spell context key bind,
     * @return true to end this effect
     */
    public boolean spellContextAction(EntityPlayer caster) {
        RayTraceResult target = RayTraceHelper.getTargetEntityOrBlock(caster, 32);
        if(target.typeOfHit == RayTraceResult.Type.ENTITY && target.entityHit instanceof EntityReplicate) {
            EntityReplicate replica = (EntityReplicate) target.entityHit;
            if(replica.getPlayer().getUniqueID().equals(caster.getUniqueID())) {
                replica.swapWithPlayer();
            }
        }
        return false;
    }

    public void onReplicaDeath(EntityReplicate replica) {
        if(this.replicas.containsKey(replica.getPlayer().getUniqueID())) {
            this.replicas.get(replica.getPlayer().getUniqueID()).remove(replica);
        }
    }
}
