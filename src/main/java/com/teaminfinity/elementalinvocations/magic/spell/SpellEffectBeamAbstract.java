package com.teaminfinity.elementalinvocations.magic.spell;

import com.infinityraider.infinitylib.network.NetworkWrapper;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.network.MessageStartStopBeam;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

import javax.annotation.Nullable;

public abstract class SpellEffectBeamAbstract implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        RayTraceResult target = RayTraceHelper.getTargetEntityOrBlock(caster, getBeamRange(caster, potencies, channelTick));
        boolean result = this.apply(caster, potencies, channelTick, target);
        if(channelTick <= 0) {
            if(result) {
                this.sendStartMessage(caster, potencies);
            }
        } else if(!result) {
            this.sendStopMessage(caster, potencies, channelTick);
        }
        return result;
    }

    protected abstract boolean apply(EntityPlayer caster, int[] potencies, int channelTick, @Nullable RayTraceResult target);

    protected abstract double getBeamRange(EntityPlayer caster, int[] potencies, int channelTick);

    protected void sendStartMessage(EntityPlayer caster, int[] potencies) {
        NetworkWrapper.getInstance().sendToAll(new MessageStartStopBeam(caster, potencies));
    }

    protected void sendStopMessage(EntityPlayer caster, int[] potencies, int channelTick) {
        NetworkWrapper.getInstance().sendToAll(new MessageStartStopBeam(caster, potencies, channelTick));
    }
}
