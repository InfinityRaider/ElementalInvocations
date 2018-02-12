package com.infinityraider.elementalinvocations.magic.spell.air;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.entity.EntityBallLightning;
import net.minecraft.entity.player.EntityPlayer;

public class EffectBallLightning implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        EntityBallLightning ball = new EntityBallLightning(caster, potencies.getPotency(Element.AIR), potencies.getPotency(Element.WATER));
        caster.getEntityWorld().spawnEntityInWorld(ball);
        caster.startRiding(ball, true);
        return false;
    }
}
