package com.teaminfinity.elementalinvocations.magic.spell.death;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import net.minecraft.entity.player.EntityPlayer;
import com.teaminfinity.elementalinvocations.api.souls.ISoulCollection;

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
