package com.teaminfinity.elementalinvocations.magic.spell.death;

import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import net.minecraft.entity.player.EntityPlayer;

public class EffectMalifice implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        return false;
    }
}
