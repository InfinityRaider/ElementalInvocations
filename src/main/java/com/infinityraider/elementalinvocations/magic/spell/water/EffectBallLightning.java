package com.infinityraider.elementalinvocations.magic.spell.water;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.entity.EntityBallLightning;
import net.minecraft.entity.player.EntityPlayer;

public class EffectBallLightning implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        EntityBallLightning ball = new EntityBallLightning(caster, potencies[Element.AIR.ordinal()], potencies[Element.WATER.ordinal()]);
        caster.getEntityWorld().spawnEntityInWorld(ball);
        caster.startRiding(ball, true);
        return false;
    }
}
