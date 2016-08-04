package com.teaminfinity.elementalinvocations.magic.generic;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class MagicDamage {
    private DamageSource source;
    private float amount;

    public MagicDamage(DamageSource source, float amount) {
        this.source = source;
        this.amount = amount;
    }

    public DamageSource getSource() {
        return this.source;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void applyToEntity(EntityLivingBase entity) {
        entity.attackEntityFrom(this.source, this.amount);
    }
}
