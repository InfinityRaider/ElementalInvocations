package com.infinityraider.elementalinvocations.magic.spell.death;

import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import net.minecraft.entity.player.EntityPlayer;

public class EffectRequiem implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        return false;
    }
}
