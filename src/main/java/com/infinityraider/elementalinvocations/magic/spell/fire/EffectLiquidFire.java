package com.infinityraider.elementalinvocations.magic.spell.fire;

import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import net.minecraft.entity.player.EntityPlayer;

public class EffectLiquidFire implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        return false;

    }
}
