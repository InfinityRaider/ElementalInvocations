package com.teaminfinity.elementalinvocations.magic.spell.death;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.IPlayerSoulCollection;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import net.minecraft.entity.player.EntityPlayer;

public class EffectConsumeSoul implements ISpellEffect {
    @Override
    public void apply(EntityPlayer caster, int[] potencies) {
        IPlayerSoulCollection collection = PlayerSoulCollectionProvider.getSoulCollection(caster);
        if(collection != null && collection.getSoulCount() > 0) {
            collection.removeSoul();
            float potency = (float) (potencies[Element.DEATH.ordinal()] + potencies[Element.LIFE.ordinal()]);
            caster.heal(potency / 5);
        }
    }
}
