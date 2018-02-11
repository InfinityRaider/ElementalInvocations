package com.infinityraider.elementalinvocations.magic;


import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IMagicDamageHandler;
import com.infinityraider.elementalinvocations.api.souls.ISoul;
import com.infinityraider.elementalinvocations.api.souls.ISoulCollection;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import com.infinityraider.elementalinvocations.handler.ConfigurationHandler;
import com.infinityraider.elementalinvocations.magic.spell.death.BasicSoul;
import com.infinityraider.elementalinvocations.reference.Constants;
import com.infinityraider.elementalinvocations.registry.PotionRegistry;
import com.infinityraider.infinitylib.utility.DamageDealer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

public class MagicDamageHandler implements IMagicDamageHandler {
    private static final MagicDamageHandler INSTANCE = new MagicDamageHandler();

    public static MagicDamageHandler getInstance() {
        return INSTANCE;
    }

    private final Map<Element, DamageDealerElemental> damageDealers;

    private MagicDamageHandler() {
        this.damageDealers = new HashMap<>();
        for(Element element : Element.values()) {
            this.damageDealers.put(element, this.createDamageDealer(element));
        }
    }

    public DamageDealerElemental getDamageDealer(Element element) {
        return this.damageDealers.get(element);
    }

    @Override
    public void dealDamage(Entity target, float amount, Element element, int potency) {
        this.getDamageDealer(element).apply(target, amount, potency);
    }

    @Override
    public void dealDamage(Entity target, float amount, Element element, int potency, Vec3d dir) {
        this.getDamageDealer(element).apply(target, amount, dir, potency);
    }

    @Override
    public void dealDamage(Entity target, float amount, Entity source, Element element, int potency) {
        Entity cause;
        if(source instanceof EntityThrowable) {
            cause = ((EntityThrowable) source).getThrower();
        } else {
            cause = source;
        }
        this.dealDamage(target, amount, source, cause, element, potency);
    }

    @Override
    public void dealDamage(Entity target, float amount, Entity source, Element element, int potency, Vec3d dir) {
        Entity cause;
        if(source instanceof EntityThrowable) {
            cause = ((EntityThrowable) source).getThrower();
        } else {
            cause = source;
        }
        this.dealDamage(target, amount, source, cause, element, potency, dir);
    }

    @Override
    public void dealDamage(Entity target, float amount, Entity source, Entity cause, Element element, int potency) {
        this.getDamageDealer(element).apply(target, source, cause, amount, potency);
    }

    @Override
    public void dealDamage(Entity target, float amount, Entity source, Entity cause, Element element, int potency, Vec3d dir) {
        this.getDamageDealer(element).apply(target, source, cause, amount, dir, potency);
    }

    public DamageDealerElemental createDamageDealer(Element element) {
        switch(element) {
            case LIFE: return createDamageDealerLife();
            case AIR: return createDamageDealerAir();
            case FIRE: return createDamageDealerFire();
            case DEATH: return createDamageDealerDeath();
            case EARTH: return createDamageDealerEarth();
            case WATER: return createDamageDealerWater();
            default: return new DamageDealerElemental(element);
        }
    }

    protected DamageDealerElemental createDamageDealerLife() {
        DamageDealerElemental dmg = new DamageDealerElemental(Element.LIFE);
        dmg.setDamageCallback((target, damage, amount) -> {
            if(target instanceof EntityLivingBase && ((EntityLivingBase) target).isEntityUndead()) {
                return amount*2;
            } else {
                if(target instanceof EntityLivingBase) {
                    ((EntityLivingBase) target).heal(amount);
                }
                return 0;
            }
        });
        return dmg;
    }

    protected DamageDealerElemental createDamageDealerAir() {
        DamageDealerElemental dmg = new DamageDealerElemental(Element.FIRE);
        dmg.setBypassArmor(true);
        dmg.setDamageCallback((target, damage, amount) -> {
            if(target instanceof EntityLivingBase && damage instanceof DamageSourceElemental) {
                EntityLivingBase entity = (EntityLivingBase) target;
                int potency = ((DamageSourceElemental) damage).getPotency();
                if(this.rollSecondaryEffect(entity, potency)) {
                    entity.addPotionEffect(new PotionEffect(PotionRegistry.getInstance().POTION_CONFUSION, 20 * potency));
                }
            }
        });
        return dmg;
    }

    protected DamageDealerElemental createDamageDealerFire() {
        DamageDealerElemental dmg = new DamageDealerElemental(Element.FIRE);
        dmg.setFireDamage(true);
        dmg.setDamageCallback((target, damage, amount) -> {
            if(target instanceof EntityLivingBase && damage instanceof DamageSourceElemental) {
                EntityLivingBase entity = (EntityLivingBase) target;
                int potency = ((DamageSourceElemental) damage).getPotency();
                if(this.rollSecondaryEffect(entity, potency)) {
                    target.setFire(potency);
                }
            }
        });
        return dmg;
    }

    protected DamageDealerElemental createDamageDealerDeath() {
        DamageDealerElemental dmg = new DamageDealerElemental(Element.DEATH);
        dmg.setAbsolute(true);
        dmg.setDamageCallback((target, damage, amount) -> {
            if(target.isEntityAlive()) {
                return;
            }
            Entity cause = damage.getEntity();
            if(cause instanceof EntityPlayer) {
                ISoulCollection souls = CapabilityPlayerSoulCollection.getSoulCollection((EntityPlayer) cause);
                //reap target soul
                if(souls != null) {
                    ISoul soul = new BasicSoul(target.getName());
                    ElementalInvocations.instance.getLogger().debug("Reaped Soul: {0}!", soul.getName());
                    souls.addSoul(soul);
                }
                //steal the target's souls
                if(target instanceof EntityPlayer) {
                    ISoulCollection targetSouls = CapabilityPlayerSoulCollection.getSoulCollection((EntityPlayer) target);
                    targetSouls.releaseSouls().forEach(souls::addSoul);
                }
            }
        });
        return dmg;
    }

    protected DamageDealerElemental createDamageDealerEarth() {
        DamageDealerElemental dmg = new DamageDealerElemental(Element.EARTH);
        dmg.setScalable(true);
        dmg.setMagicDamage(false);
        dmg.setDamageCallback((target, damage, amount) -> {
            Vec3d dir = damage.getDirection();
            if(dir != null && target instanceof EntityLivingBase && damage instanceof DamageSourceElemental) {
                int potency = ((DamageSourceElemental) damage).getPotency();
                ((EntityLivingBase) target).knockBack(damage.getEntity(), ((float) potency)/Constants.CORE_TIERS, -dir.xCoord, -dir.zCoord);
            }
        });
        return dmg;
    }

    protected DamageDealerElemental createDamageDealerWater() {
        DamageDealerElemental dmg = new DamageDealerElemental(Element.WATER);
        dmg.setDamageCallback((target, damage, amount) -> {
            if(target instanceof EntityLivingBase) {
                target.extinguish();
                if(damage instanceof DamageSourceElemental) {
                    int potency = ((DamageSourceElemental) damage).getPotency();
                    ((EntityLivingBase) target).addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("slowness"), potency));
                }
            }
        });
        return dmg;
    }

    protected boolean rollSecondaryEffect(EntityLivingBase entity, int potency) {
        return entity.getRNG().nextInt(Constants.CORE_TIERS*Constants.CORE_TIERS) <= potency;
    }

    private static class DamageDealerElemental extends DamageDealer {
        public DamageDealerElemental(Element element) {
            super("dmg.ei." + element.name().toLowerCase(), ConfigurationHandler.getInstance().damageMultiplier);
            this.setMagicDamage(true);
        }

        public void apply(Entity target, float amount, int potency) {
            this.apply(target, this.createDamage(potency), amount);
        }

        public void apply(Entity target, float amount, Vec3d dir, int potency) {
            this.apply(target, this.createDamage(potency).setDirection(dir), amount);
        }

        public void apply(Entity target, Entity source, float amount, int potency) {
            this.apply(target, this.createDamage(source, potency), amount);
        }

        public void apply(Entity target, Entity source, float amount, Vec3d dir, int potency) {
            this.apply(target, this.createDamage(source, potency).setDirection(dir), amount);
        }

        public void apply(Entity target, Entity source, Entity cause, float amount, int potency) {
            this.apply(target, this.createDamage(source, cause, potency), amount);
        }

        public void apply(Entity target, Entity source, Entity cause, float amount, Vec3d dir, int potency) {
            this.apply(target, this.createDamage(source, cause, potency).setDirection(dir), amount);
        }

        protected DamageDealer.InfinityDamageSource createDamage(Entity source, int potency) {
            if(source instanceof EntityThrowable) {
                return this.createDamage(source, (((EntityThrowable) source).getThrower()), potency);
            } else {
                return this.createDamage(source, source, potency);
            }
        }

        protected DamageDealer.InfinityDamageSource createDamage(Entity source, Entity cause, int potency) {
            DamageDealer.InfinityDamageSource dmg = this.createDamage(potency);
            return dmg.setSource(source).setCause(cause);
        }

        protected DamageDealer.InfinityDamageSource createDamage(int potency) {
            return applySettings(new DamageSourceElemental(this.getName(), potency));
        }
    }

    private static class DamageSourceElemental extends DamageDealer.InfinityDamageSource {
        private final int potency;

        protected DamageSourceElemental(String damageTypeIn, int potency) {
            super(damageTypeIn);
            this.potency = potency;
        }

        public int getPotency() {
            return this.potency;
        }
    }
}