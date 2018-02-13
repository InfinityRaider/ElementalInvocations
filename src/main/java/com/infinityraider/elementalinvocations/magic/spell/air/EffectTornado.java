package com.infinityraider.elementalinvocations.magic.spell.air;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.entity.EntityTornado;
import net.minecraft.entity.player.EntityPlayer;

public class EffectTornado implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        EntityTornado tornado = new EntityTornado(caster, potencies.getPotency(Element.AIR), potencies.getPotency(Element.DEATH));
        caster.getEntityWorld().spawnEntityInWorld(tornado);
        return false;
    }

}