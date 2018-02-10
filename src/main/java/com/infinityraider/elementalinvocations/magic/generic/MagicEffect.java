package com.infinityraider.elementalinvocations.magic.generic;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.handler.DamageHandler;
import com.infinityraider.elementalinvocations.magic.generic.effect.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class MagicEffect {
    protected static final int MAX_WEIGHT = 25;

    public static final ElementEffect FIRE = new ElementEffectFire();
    public static final ElementEffect WATER = new ElementEffectWater();
    public static final ElementEffect AIR = new ElementEffectAir();
    public static final ElementEffect EARTH = new ElementEffectEarth();
    public static final ElementEffect DEATH = new ElementEffectDeath();
    public static final ElementEffect LIFE = new ElementEffectLife();

    private final EntityPlayer caster;
    private final EntityLivingBase target;
    private final Vec3d direction;
    private final IPotencyMap potencies;

    public MagicEffect(EntityPlayer caster, EntityLivingBase target, Vec3d direction, IPotencyMap potencies) {
        this.caster = caster;
        this.target = target;
        this.direction = direction;
        this.potencies = potencies;
    }

    public int getPotency(Element element) {
        return this.potencies.getPotency(element);
    }

    public void apply() {
        //calculate base damage
        for(Element element : Element.values()) {
            int potency = this.getPotency(element);
            if(potency > 0) {
                ElementEffect secondary = getSecondaryFromElement(element);
                boolean applySecondary = caster.getRNG().nextInt(MAX_WEIGHT) <= potency;
                //apply secondary effect before applying damage
                secondary.applyEffectPre(this, caster, target, potency, applySecondary);
                //apply damage
                DamageHandler.getInstance().getDamageDealer(element).applyDamage(target, caster, potency);
                //apply secondary effect after applying damage
                secondary.applyEffectPost(this, caster, target, potency, applySecondary);
            }
        }
    }

    public Vec3d getDirection() {
        return direction;
    }

    public  static ElementEffect getSecondaryFromElement(Element element) {
        switch(element) {
            case FIRE: return FIRE;
            case WATER: return WATER;
            case AIR: return AIR;
            case EARTH: return EARTH;
            case DEATH: return DEATH;
            case LIFE: return LIFE;
        }
        return null;
    }

}
