package com.infinityraider.elementalinvocations.magic.generic.effect;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.magic.generic.MagicDamage;
import com.infinityraider.elementalinvocations.magic.generic.MagicEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ElementEffectLife extends ElementEffect {
    public ElementEffectLife() {
        super(Element.LIFE);
    }

    @Override
    public void applyEffectPre(MagicEffect effect, EntityPlayer caster, EntityLivingBase target, int potency, boolean secondary) {
        float healing = 0;
        for(MagicDamage damage : effect.getAppliedDamage()) {
            healing = healing + damage.getAmount();
        }
        effect.getAppliedDamage().clear();
        target.heal(healing);
        if(secondary) {
            caster.heal(healing);
        }
    }

    @Override
    public void applyEffectPost(MagicEffect effect, EntityPlayer caster, EntityLivingBase target, int potency, boolean secondary) {

    }
}
