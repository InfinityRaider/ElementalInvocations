package com.teaminfinity.elementalinvocations.magic.spell.earth;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.registry.PotionRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

public class EffectLivingArmor implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        caster.addPotionEffect(new PotionEffect(PotionRegistry.getInstance().POTION_LIVING_ARMOR, 20*potencies[Element.LIFE.ordinal()], potencies[Element.EARTH.ordinal()]));
        return false;
    }
}
