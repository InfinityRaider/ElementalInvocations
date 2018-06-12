package com.infinityraider.elementalinvocations.magic.spell.life;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.handler.HexHandler;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

public class EffectHex implements ISpellEffect {

    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        int air = potencies.getPotency(Element.AIR) / 2;
        int life = potencies.getPotency(Element.LIFE) / 3;
        RayTraceResult hit = RayTraceHelper.getTargetEntityOrBlock(caster, 8*(air + 1));
        if(hit != null && hit.typeOfHit == RayTraceResult.Type.ENTITY && hit.entityHit instanceof EntityLivingBase) {
            HexHandler.getInstance().hexEntity((EntityLivingBase) hit.entityHit, 20*life);
        }
        return false;
    }
}