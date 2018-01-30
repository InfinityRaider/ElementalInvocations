package com.infinityraider.elementalinvocations.magic.spell.death;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.souls.ISoulCollection;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import net.minecraft.entity.player.EntityPlayer;

public class EffectConsumeSoul implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        ISoulCollection collection = CapabilityPlayerSoulCollection.getSoulCollection(caster);
        if(collection != null && collection.getSoulCount() > 0) {
            collection.removeSoul();
            float potency = (float) (potencies[Element.DEATH.ordinal()] + potencies[Element.LIFE.ordinal()]);
            caster.heal(potency / 5);
        }
        return false;
    }
}
