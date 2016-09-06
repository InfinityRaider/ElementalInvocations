package com.teaminfinity.elementalinvocations.magic.spell.death;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.souls.ISoulCollection;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.entity.EntityReplicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class EffectReplicate implements ISpellEffect {
    private static final int REPLICA_LIFETIME_PER_LEVEL = 10;

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
        for(int i = 0; i < amount; i++) {
            EntityLivingBase entity;
            if(i == 0) {
                entity = caster;
            } else {
                replicas.add(new EntityReplicate(caster, potencyAir *20 * REPLICA_LIFETIME_PER_LEVEL / 2));
                entity = replicas.get(i - 1);
                collection.removeSoul();
                caster.getEntityWorld().spawnEntityInWorld(entity);
            }
            double angle = (yaw + (i*360/amount) + offsetForIndex(i)) % 360;
            double newX = x + radius*Math.cos(Math.toRadians(angle));
            double newZ = z + radius*Math.sin(Math.toRadians(angle));
            entity.moveEntity(newX, y, newZ);
        }
        int swap = caster.getRNG().nextInt(replicas.size() + 1);
        if(swap < replicas.size()) {
            replicas.get(swap).swapWithPlayer();
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
}
