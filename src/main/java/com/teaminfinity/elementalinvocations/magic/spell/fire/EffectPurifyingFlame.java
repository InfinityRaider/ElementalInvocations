package com.teaminfinity.elementalinvocations.magic.spell.fire;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.utility.TargetHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;

public class EffectPurifyingFlame implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        RayTraceResult target = TargetHelper.getTargetEntityOrBlock(caster, 64);
        if(target != null && target.entityHit != null && (target.entityHit instanceof EntityLivingBase)) {
            EntityLivingBase entity = (EntityLivingBase) target.entityHit;
            entity.attackEntityFrom(new DamageSourcePurifyingFlame(), potencies[Element.FIRE.ordinal()]);
            entity.addPotionEffect(new PotionEffect(Potion.getPotionById(10), 15 - potencies[Element.LIFE.ordinal()]));
        }
        return false;
    }

    public static class DamageSourcePurifyingFlame extends DamageSource {
        public DamageSourcePurifyingFlame() {
            super("non purer");
            this.setMagicDamage();
            this.setFireDamage();
            this.setDamageBypassesArmor();
        }
    }
}
