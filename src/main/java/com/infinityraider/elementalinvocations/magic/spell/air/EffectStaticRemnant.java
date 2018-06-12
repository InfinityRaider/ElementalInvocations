package com.infinityraider.elementalinvocations.magic.spell.air;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.entity.EntityStaticRemnant;
import net.minecraft.entity.player.EntityPlayer;

public class EffectStaticRemnant implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        EntityStaticRemnant remnant = new EntityStaticRemnant(caster, potencies.getPotency(Element.AIR), potencies.getPotency(Element.LIFE));
        caster.getEntityWorld().spawnEntity(remnant);
        return false;

    }
}
