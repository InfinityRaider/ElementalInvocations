package com.teaminfinity.elementalinvocations.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public interface IDamageReductor {
    float getDamageReduction(EntityLivingBase entity, DamageSource source, float amount, PotionEffect effect);
}
