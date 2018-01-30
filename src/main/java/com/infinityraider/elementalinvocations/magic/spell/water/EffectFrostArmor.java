package com.infinityraider.elementalinvocations.magic.spell.water;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.registry.PotionRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

public class EffectFrostArmor implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        caster.addPotionEffect(new PotionEffect(PotionRegistry.getInstance().POTION_FROST_ARMOR, potencies[Element.WATER.ordinal()] * 20, potencies[Element.EARTH.ordinal()]));
        return false;
    }
}
