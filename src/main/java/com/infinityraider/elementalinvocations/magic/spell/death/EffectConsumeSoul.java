package com.infinityraider.elementalinvocations.magic.spell.death;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.souls.ISoulCollection;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import net.minecraft.entity.player.EntityPlayer;

public class EffectConsumeSoul implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        ISoulCollection collection = CapabilityPlayerSoulCollection.getSoulCollection(caster);
        if(collection != null && collection.getSoulCount() > 0) {
            collection.removeSoul();
            float potency = (float) (potencies.getPotency(Element.DEATH) + potencies.getPotency(Element.LIFE));
            caster.heal(potency / 5);
        }
        return false;
    }
}
