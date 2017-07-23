package com.teaminfinity.elementalinvocations.magic.spell.water;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.entity.EntityBallLightning;
import net.minecraft.entity.player.EntityPlayer;

public class EffectBallLightning implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        EntityBallLightning ball = new EntityBallLightning(caster, potencies[Element.AIR.ordinal()], potencies[Element.WATER.ordinal()]);
        caster.getEntityWorld().spawnEntity(ball);
        caster.startRiding(ball, true);
        return false;
    }
}
