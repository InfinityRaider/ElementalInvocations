package com.infinityraider.elementalinvocations.magic.spell;

import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.network.MessageUpdateBeamRange;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import com.infinityraider.elementalinvocations.network.MessageStartStopBeam;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class SpellEffectBeamAbstract implements ISpellEffect {
    private Map<UUID, Double> beamLengths = new HashMap<>();

    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        double range = this.getBeamRange(caster, potencies, channelTick);
        RayTraceResult target = RayTraceHelper.getTargetEntityOrBlock(caster, range);
        boolean result = this.apply(caster, potencies, channelTick, target);
        if(channelTick <= 0) {
            if(result) {
                this.beamLengths.put(caster.getUniqueID(), range);
                this.sendStartMessage(caster, potencies, range);
            }
        } else if(!result) {
            this.beamLengths.remove(caster.getUniqueID());
            this.sendStopMessage(caster, potencies, channelTick);
        } else {
            if(this.beamLengths.get(caster.getUniqueID()) != range) {
                this.beamLengths.put(caster.getUniqueID(), range);
                this.sendBeamRangeUpdateMessage(caster, range);
            }
        }
        return result;
    }

    @Override
    public void onPlayerStopChanneling(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        this.afterPlayerStoppedChanneling(caster, potencies, channelTick);
        this.sendStopMessage(caster, potencies, channelTick);
    }

    protected abstract boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick, @Nullable RayTraceResult target);

    protected abstract void afterPlayerStoppedChanneling(EntityPlayer caster, IPotencyMap potencies, int channelTick);

    protected abstract double getBeamRange(EntityPlayer caster, IPotencyMap potencies, int channelTick);

    protected void sendStartMessage(EntityPlayer caster, IPotencyMap potencies, double range) {
        new MessageStartStopBeam(caster, potencies, range).sendToAll();
    }

    protected void sendStopMessage(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        new MessageStartStopBeam(caster, potencies, channelTick).sendToAll();
    }

    protected void sendBeamRangeUpdateMessage(EntityPlayer caster, double range) {
       new MessageUpdateBeamRange(caster, range).sendToAll();
    }
}
