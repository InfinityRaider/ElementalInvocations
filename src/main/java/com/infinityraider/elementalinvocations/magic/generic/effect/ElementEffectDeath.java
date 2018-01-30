package com.infinityraider.elementalinvocations.magic.generic.effect;

import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.souls.ISoul;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import com.infinityraider.elementalinvocations.magic.generic.MagicEffect;
import com.infinityraider.elementalinvocations.magic.spell.death.BasicSoul;
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
