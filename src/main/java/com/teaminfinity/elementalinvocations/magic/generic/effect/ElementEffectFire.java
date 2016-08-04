package com.teaminfinity.elementalinvocations.magic.generic.effect;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.magic.generic.MagicDamage;
import com.teaminfinity.elementalinvocations.magic.generic.MagicEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ElementEffectFire extends ElementEffect {
    public ElementEffectFire() {
        super(Element.FIRE);
    }

    @Override
    public void applyEffectPre(MagicEffect effect, EntityPlayer caster, EntityLivingBase target, int potency, boolean secondary) {
        for(MagicDamage dmg : effect.getAppliedDamage()) {
            dmg.setAmount(dmg.getAmount() * 3);
        }
    }

    @Override
    public void applyEffectPost(MagicEffect effect, EntityPlayer caster, EntityLivingBase target, int potency, boolean secondary) {

    }
}
