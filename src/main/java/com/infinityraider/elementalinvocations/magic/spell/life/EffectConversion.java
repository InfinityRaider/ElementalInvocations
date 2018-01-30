package com.infinityraider.elementalinvocations.magic.spell.life;

import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import net.minecraft.entity.player.EntityPlayer;

public class EffectConversion implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        return false;

    }
}
