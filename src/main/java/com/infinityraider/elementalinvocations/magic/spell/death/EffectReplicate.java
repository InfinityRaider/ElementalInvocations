package com.infinityraider.elementalinvocations.magic.spell.death;

import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import com.infinityraider.elementalinvocations.registry.SoundRegistry;
import com.infinityraider.infinitylib.sound.ModSoundHandler;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.souls.ISoulCollection;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.entity.EntityReplicate;
import com.infinityraider.elementalinvocations.utility.PlayerHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;

import java.util.*;

public class EffectReplicate implements ISpellEffect {
    private static final int REPLICA_LIFETIME_PER_LEVEL = 10;

    private static final Map<UUID, List<EntityReplicate>> replicas = new HashMap<>();

    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        int potencyDeath = potencies.getPotency(Element.DEATH);
        int potencyLife = potencies.getPotency(Element.LIFE);
        ISoulCollection collection = CapabilityPlayerSoulCollection.getSoulCollection(caster);
        int amount = Math.min(potencyDeath/3, collection.getSoulCount());
        if(amount <= 0) {
            return false;
        }
        double x = caster.posX;
        double y = caster.posY;
        double z = caster.posZ;
        float yaw = caster.rotationYaw;
        double radius = 2.5F;
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
                replicas.add(new EntityReplicate(caster, this, potencyLife *20 * REPLICA_LIFETIME_PER_LEVEL / 2));
                entity = replicas.get(i - 1);
                collection.removeSoul();
                caster.getEntityWorld().spawnEntity(entity);
            }
            entity.setPosition(newX, y, newZ);
        }
        int swap = caster.getRNG().nextInt(replicas.size() + 1);
        if(swap < replicas.size()) {
            replicas.get(swap).swapWithPlayer();
        }
        if(!EffectReplicate.replicas.containsKey(caster.getUniqueID())) {
            EffectReplicate.replicas.put(caster.getUniqueID(), replicas);
        } else {
            EffectReplicate.replicas.get(caster.getUniqueID()).addAll(replicas);
        }
        if(replicas.size() > 0) {
            ModSoundHandler.getInstance().playSoundAtEntityOnce(caster, SoundRegistry.getInstance().SOUND_REPLICATE, SoundCategory.PLAYERS);
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
        return !EffectReplicate.replicas.containsKey(caster.getUniqueID()) || EffectReplicate.replicas.get(caster.getUniqueID()).isEmpty();
    }

    /**
     * Called when the caster presses the spell context key bind,
     * @return true to end this effect
     */
    public boolean spellContextAction(EntityPlayer caster) {
        RayTraceResult target = RayTraceHelper.getTargetEntityOrBlock(caster, 32, EntityReplicate.class);
        if(target.typeOfHit == RayTraceResult.Type.ENTITY && target.entityHit instanceof EntityReplicate) {
            EntityReplicate replica = (EntityReplicate) target.entityHit;
            if(replica.getPlayer().getUniqueID().equals(caster.getUniqueID())) {
                replica.swapWithPlayer();
            }
        }
        return false;
    }

    public void onReplicaDeath(EntityReplicate replica) {
        if(EffectReplicate.replicas.containsKey(replica.getPlayer().getUniqueID())) {
            EffectReplicate.replicas.get(replica.getPlayer().getUniqueID()).remove(replica);
        }
    }
}
