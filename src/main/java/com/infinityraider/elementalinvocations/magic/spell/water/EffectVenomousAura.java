package com.infinityraider.elementalinvocations.magic.spell.water;

import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import net.minecraft.entity.player.EntityPlayer;

public class EffectVenomousAura implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        return false;

    }
}
