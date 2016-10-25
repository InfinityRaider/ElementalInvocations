package com.teaminfinity.elementalinvocations.magic.generic.effect;

import com.teaminfinity.elementalinvocations.ElementalInvocations;
import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.souls.ISoul;
import com.teaminfinity.elementalinvocations.magic.generic.MagicEffect;
import com.teaminfinity.elementalinvocations.magic.spell.death.BasicSoul;
import com.teaminfinity.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ElementEffectDeath extends ElementEffect {
    public ElementEffectDeath() {
        super(Element.DEATH);
    }

    @Override
    public void applyEffectPre(MagicEffect effect, EntityPlayer caster, EntityLivingBase target, int potency, boolean secondary) {

    }

    @Override
    public void applyEffectPost(MagicEffect effect, EntityPlayer caster, EntityLivingBase target, int potency, boolean secondary) {
        if(secondary && !target.isEntityAlive()) {
            ISoul soul = new BasicSoul(target.getName());
            ElementalInvocations.instance.getLogger().debug("Reaped Soul: {0}!", soul.getName());
			CapabilityPlayerSoulCollection.getSoulCollection(caster).addSoul(soul);
        }
    }
}
