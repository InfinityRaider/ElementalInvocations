package com.infinityraider.elementalinvocations.magic.generic;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.magic.generic.effect.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

import java.util.*;

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
    private final int[] potencies;

    private Map<Element, Boolean> secondaryMask;

    private Set<ElementEffect> secondaryEffects;

    private List<MagicDamage> damageList;

    public MagicEffect(EntityPlayer caster, EntityLivingBase target, Vec3d direction, int[] potencies) {
        this.caster = caster;
        this.target = target;
        this.direction = direction;
        this.potencies = potencies;
        this.secondaryEffects = new TreeSet<>();
        this.secondaryMask = new HashMap<>();
        this.damageList = new ArrayList<>();
        this.determineSecondaryEffects();
    }

    private void determineSecondaryEffects() {
        for(Element element : Element.values()) {
            if(potencies[element.ordinal()] > 0) {
                secondaryEffects.add(getSecondaryFromElement(element));
            }
        }
    }

    public void apply() {
        //calculate base damage
        for(Element element : Element.values()) {
            int potency = potencies[element.ordinal()];
            if(potency > 0) {
                damageList.add(new MagicDamage(new DamageSourceMagic(element, caster), potency));
                secondaryMask.put(element, caster.getRNG().nextInt(MAX_WEIGHT) <= potency);
            }
        }
        //apply secondary effects before applying damage
        for(ElementEffect effect : secondaryEffects) {
            effect.applyEffectPre(this, caster, target, potencies[effect.element().ordinal()], secondaryMask.get(effect.element()));
        }
        //apply damage
        for(MagicDamage dmg : damageList) {
            dmg.applyToEntity(target);
        }
        //apply secondary effects after applying damage
        for(ElementEffect effect : secondaryEffects) {
            effect.applyEffectPost(this, caster, target, potencies[effect.element().ordinal()], secondaryMask.get(effect.element()));
        }
    }

    public Vec3d getDirection() {
        return direction;
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
