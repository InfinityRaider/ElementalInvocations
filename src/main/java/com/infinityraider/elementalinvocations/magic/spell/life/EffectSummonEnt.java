package com.infinityraider.elementalinvocations.magic.spell.life;

import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.magic.spell.SpellEffectBeamAbstract;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

import javax.annotation.Nullable;

public class EffectSummonEnt extends SpellEffectBeamAbstract {
    @Override
    protected boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick, @Nullable RayTraceResult target) {
        return false;
    }

    @Override
    protected void afterPlayerStoppedChanneling(EntityPlayer caster, IPotencyMap potencies, int channelTick) {}

    @Override
    protected double getBeamRange(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        return 0;
    }
}
