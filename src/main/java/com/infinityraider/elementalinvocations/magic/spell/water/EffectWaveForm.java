package com.infinityraider.elementalinvocations.magic.spell.water;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.entity.EntityWaveForm;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

public class EffectWaveForm implements ISpellEffect {
    private final Map<EntityPlayer, EntityWaveForm> waveForms;

    public EffectWaveForm() {
        waveForms = new HashMap<>();
    }

    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        int potency = potencies[Element.WATER.ordinal()];
        if(channelTick == 0) {
            EntityWaveForm waveForm = new EntityWaveForm(caster, potency);
            caster.getEntityWorld().spawnEntityInWorld(waveForm);
            caster.startRiding(waveForm);
            waveForms.put(caster, waveForm);
        }
        EntityWaveForm waveForm = waveForms.get(caster);
        if(waveForm == null) {
            return false;
        }
        if(channelTick >= potency * 4) {
            waveForm.setDead();
            waveForms.remove(caster);
            return false;
        } else {
            waveForm.channelUpdate(caster);
            return true;
        }
    }
}
