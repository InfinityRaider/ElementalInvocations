package com.infinityraider.elementalinvocations.magic.spell.air;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.entity.EntityChainLightning;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EffectChainLightning implements ISpellEffect {
    private static final Map<UUID, EntityChainLightning> chains = new HashMap<>();

    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        if(chains.containsKey(caster.getUniqueID())) {
            return true;
        }
        RayTraceResult target = RayTraceHelper.getTargetEntityOrBlock(caster, 5, EntityLivingBase.class);
        if(target != null && target.typeOfHit == RayTraceResult.Type.ENTITY && target.entityHit instanceof EntityLivingBase) {
            EntityChainLightning chain = new EntityChainLightning(caster, (EntityLivingBase) target.entityHit, potencies.getPotency(Element.AIR)/5);
            caster.getEntityWorld().spawnEntityInWorld(chain);
            chains.put(caster.getUniqueID(), chain);
            return true;
        }
        return false;
    }

    @Override
    public void onPlayerStopChanneling(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        if(chains.containsKey(caster.getUniqueID())) {
            EntityChainLightning chain = chains.get(caster.getUniqueID());
            if(chain != null) {
                chain.setDead();
            }
            chains.remove(caster.getUniqueID());
        }
    }

    public static void onChainStopped(EntityChainLightning chain) {
        if(chain.isMaster()) {
            if(chain.getCasterId() != null) {
                chains.remove(chain.getCasterId());
            }
        }
    }
}
