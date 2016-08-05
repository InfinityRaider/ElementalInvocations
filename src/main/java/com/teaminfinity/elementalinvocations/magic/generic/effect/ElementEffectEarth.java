package com.teaminfinity.elementalinvocations.magic.generic.effect;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.magic.generic.MagicEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ElementEffectEarth extends ElementEffect {
    public ElementEffectEarth() {
        super(Element.EARTH);
    }

    @Override
    public void applyEffectPre(MagicEffect effect, EntityPlayer caster, EntityLivingBase target, int potency, boolean secondary) {

    }

    @Override
    public void applyEffectPost(MagicEffect effect, EntityPlayer caster, EntityLivingBase target, int potency, boolean secondary) {
        if(secondary) {
            target.knockBack(caster, ((float) potency) / 3, effect.getDirection().xCoord, effect.getDirection().zCoord);
        }
    }
}
