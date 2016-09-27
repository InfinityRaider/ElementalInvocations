package com.teaminfinity.elementalinvocations.magic.spell.life;

import com.teaminfinity.elementalinvocations.magic.spell.SpellEffectBeamAbstract;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

import javax.annotation.Nullable;

public class EffectUpRootsAndRun extends SpellEffectBeamAbstract {
    @Override
    protected boolean apply(EntityPlayer caster, int[] potencies, int channelTick, @Nullable RayTraceResult target) {
        return false;
    }

    @Override
    protected double getBeamRange(EntityPlayer caster, int[] potencies, int channelTick) {
        return 0;
    }
}
