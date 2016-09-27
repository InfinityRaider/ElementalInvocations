package com.teaminfinity.elementalinvocations.magic.spell;

import com.infinityraider.infinitylib.utility.RayTraceHelper;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

public abstract class SpellEffectBeamAbstract implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        RayTraceResult target = RayTraceHelper.getTargetEntityOrBlock(caster, getBeamRange(caster, potencies, channelTick));
        boolean result = this.applyEffect(caster, potencies, channelTick);
        if(channelTick <= 0) {
            if(result) {

            }
        } else if(!result) {

        }
        return result;
    }

    protected abstract boolean applyEffect(EntityPlayer caster, int[] potencies, int channelTick);

    protected abstract double getBeamRange(EntityPlayer caster, int[] potencies, int channelTick);
}
