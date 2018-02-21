package com.infinityraider.elementalinvocations.magic.spell.fire;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.magic.MagicDamageHandler;
import com.infinityraider.elementalinvocations.magic.spell.SpellEffectBeamAbstract;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import javax.annotation.Nullable;

public class EffectFireBeam extends SpellEffectBeamAbstract {

    @Override
    protected boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick, @Nullable RayTraceResult target) {
        if(!caster.getEntityWorld().isRemote && target != null) {
            //damage and set entities on fire
            int potency = potencies.getPotency(Element.FIRE) / 5;
            if (target.entityHit != null) {
                if (potency >= 5 || channelTick % (6 - potency) == 0) {
                    int hurtResistTime = target.entityHit.hurtResistantTime;
                    target.entityHit.hurtResistantTime = 0;
                    MagicDamageHandler.getInstance().dealDamage(target.entityHit, 2, caster, Element.FIRE, potencies.getPotency(Element.FIRE));
                    target.entityHit.hurtResistantTime = hurtResistTime;
                }
                if (target.entityHit instanceof EntityLivingBase && !target.entityHit.isBurning()) {
                    target.entityHit.setFire(potency);
                }
            } else {
                //set fire to to the rain, I mean blocks
                BlockPos pos = target.getBlockPos();
                if(pos != null && target.sideHit != null && caster.getEntityWorld().isAirBlock(pos.offset(target.sideHit))) {
                    caster.getEntityWorld().setBlockState(pos.offset(target.sideHit), Blocks.FIRE.getDefaultState(), 11);
                }
            }
        }
        return true;
    }

    @Override
    protected void afterPlayerStoppedChanneling(EntityPlayer caster, IPotencyMap potencies, int channelTick) {}

    @Override
    protected double getBeamRange(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        return 32;
    }
}
