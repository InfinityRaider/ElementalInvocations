package com.teaminfinity.elementalinvocations.magic.generic;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.IMagicCharge;
import com.teaminfinity.elementalinvocations.magic.generic.effect.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.*;

public class MagicEffect {
    protected static final int MAX_WEIGHT = 50;

    public static final ElementEffect FIRE = new ElementEffectFire();
    public static final ElementEffect WATER = new ElementEffectWater();
    public static final ElementEffect AIR = new ElementEffectAir();
    public static final ElementEffect EARTH = new ElementEffectEarth();
    public static final ElementEffect DEATH = new ElementEffectDeath();
    public static final ElementEffect LIFE = new ElementEffectLife();

    private final EntityPlayer caster;
    private final EntityLivingBase target;

    private Map<Element, Integer> totalPotency;
    private Map<Element, Boolean> secondaryMask;

    private Set<ElementEffect> secondaryEffects;

    private List<MagicDamage> damageList;

    public MagicEffect(EntityPlayer caster, EntityLivingBase target) {
        this.caster = caster;
        this.target = target;
        this.totalPotency = new HashMap<>();
        this.secondaryEffects = new TreeSet<>();
        this.secondaryMask = new HashMap<>();
        this.damageList = new ArrayList<>();
    }

    public MagicEffect addChargeEffect(IMagicCharge charge) {
        if(!totalPotency.containsKey(charge.element())) {
            totalPotency.put(charge.element(), charge.level());
        } else {
            totalPotency.put(charge.element(), totalPotency.get(charge.element()) + charge.level());
        }
        secondaryEffects.add(getSecondaryFromElement(charge.element()));
        return this;
    }

    public void apply() {
        //calculate base damage
        for(Map.Entry<Element, Integer> entry : totalPotency.entrySet()) {
            damageList.add(new MagicDamage(new DamageSourceMagic(entry.getKey(), caster), entry.getValue()));
            secondaryMask.put(entry.getKey(), caster.getRNG().nextInt(MAX_WEIGHT) <= entry.getValue());
        }
        //apply secondary effects before applying damage
        for(ElementEffect effect : secondaryEffects) {
            effect.applyEffectPre(this, caster, target, totalPotency.get(effect.element()), secondaryMask.get(effect.element()));
        }
        //apply damage
        for(MagicDamage dmg : damageList) {
            dmg.applyToEntity(target);
        }
        //apply secondary effects after applying damage
        for(ElementEffect effect : secondaryEffects) {
            effect.applyEffectPost(this, caster, target, totalPotency.get(effect.element()), secondaryMask.get(effect.element()));
        }
    }

    public List<MagicDamage> getAppliedDamage() {
        return damageList;
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
