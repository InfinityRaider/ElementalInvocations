package com.infinityraider.elementalinvocations.magic.generic.effect;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.magic.generic.MagicEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class ElementEffectWater extends ElementEffect {
    private static final Potion SLOW = Potion.getPotionFromResourceLocation("slowness");

    public ElementEffectWater() {
        super(Element.WATER);
    }

    @Override
    public void applyEffectPre(MagicEffect effect, EntityPlayer caster, EntityLivingBase target, int potency, boolean secondary) {

    }

    @Override
    public void applyEffectPost(MagicEffect effect, EntityPlayer caster, EntityLivingBase target, int potency, boolean secondary) {
        target.addPotionEffect(new PotionEffect(SLOW, potency));
    }
}
