package com.infinityraider.elementalinvocations.magic.generic.effect;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.magic.generic.MagicEffect;
import com.infinityraider.elementalinvocations.registry.PotionRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

public class ElementEffectAir extends ElementEffect {
    public ElementEffectAir() {
        super(Element.AIR);
    }

    @Override
    public void applyEffectPre(MagicEffect effect, EntityPlayer caster, EntityLivingBase target, int potency, boolean secondary) {

    }

    @Override
    public void applyEffectPost(MagicEffect effect, EntityPlayer caster, EntityLivingBase target, int potency, boolean secondary) {
        if(secondary) {
            target.addPotionEffect(new PotionEffect(PotionRegistry.getInstance().POTION_CONFUSION, 20*potency));
        }
    }
}
