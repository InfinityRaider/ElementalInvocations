package com.infinityraider.elementalinvocations.magic;

import com.google.common.collect.ImmutableList;
import com.infinityraider.elementalinvocations.api.*;
import com.infinityraider.elementalinvocations.api.spells.ISpell;
import com.infinityraider.elementalinvocations.entity.EntityMagicProjectile;
import com.infinityraider.elementalinvocations.config.ModConfiguration;
import com.infinityraider.elementalinvocations.magic.spell.SpellRegistry;
import com.infinityraider.elementalinvocations.network.MessageAddCharge;
import com.infinityraider.elementalinvocations.network.MessageChargeAction;
import com.infinityraider.elementalinvocations.reference.Constants;
import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.elementalinvocations.registry.SoundRegistry;
import javafx.util.Pair;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.stream.Collectors;

public class MagicChargeConfiguration implements IChargeConfiguration {
    /** Link to the magic properties */
    private final IPlayerMagicProperties props;
    /** Potency map */
    private PotencyMap potencyMap;
    /** Coordinates of the instability point in the polar field */
    private double instX;   //x-coordinate
    private double instY;   //y-coordinate
    private double instA;   //polar angle
    private double instR;   //polar radius
    /** Coordinates of the potency limit point in the polar field */
    private double limX;    //x-coordinate
    private double limY;    //y-coordinate
    private double limA;    //polar angle
    private double limR;    //polar radius
    /** Current fizzle chance */
    private double pFizzle;
    /** Currently invoked charges */
    private Map<Element, List<IMagicCharge>> chargeMap;
    private List<IMagicCharge> charges;
    /** Fade and fizzle timers */
    private final Set<MagicEffectTimer> effectTimers;

    protected MagicChargeConfiguration(IPlayerMagicProperties properties) {
        this.props = properties;
        this.chargeMap = new HashMap<>();
        for(Element element : Element.values()) {
            this.chargeMap.put(element, new ArrayList<>());
        }
        this.charges = new ArrayList<>();
        this.clearCharges();
        this.effectTimers = new HashSet<>();
    }

    public IPlayerMagicProperties getProperties() {
        return this.props;
    }

    public EntityPlayer getPlayer() {
        return this.getProperties().getPlayer();
    }

    public Set<MagicEffectTimer> getEffectTimers() {
        return this.effectTimers;
    }

    @Override
    public void updateTick() {
        if (!this.getPlayer().getEntityWorld().isRemote && !this.getCharges().isEmpty() && this.fizzleCheck()) {
            this.fizzle();
        }
        this.effectTimers.removeIf(MagicEffectTimer::decrement);
    }

    @Override
    public void invoke() {
        if (getCharges().size() <= 0) {
            return;
        }
        Optional<ISpell> spell = getSpell();
        if (!this.getPlayer().getEntityWorld().isRemote) {
            if (spell.isPresent()) {
                spell.get().invoke(getPlayer(), this.getPotencyMap());
            } else {
                this.getPlayer().getEntityWorld().spawnEntity(new EntityMagicProjectile(this.getPlayer(), this.getPotencyMap()));
            }
            new MessageChargeAction(getPlayer(), EnumMagicChargeAction.INVOKE).sendToAll();
            this.getPlayer().getEntityWorld().playSound(null, this.getPlayer().posX, this.getPlayer().posY, this.getPlayer().posZ,
                    SoundRegistry.getInstance().SOUND_INVOKE, SoundCategory.PLAYERS, 0.5F, 0.5F);
        }
        this.addExperienceOnCast(spell.isPresent());
        this.effectTimers.add(MagicEffectTimer.Invoke(this));
        this.clearCharges();
    }

    @Override
    public void fade() {
        if(!this.getPlayer().getEntityWorld().isRemote) {
            new MessageChargeAction(this.getPlayer(), EnumMagicChargeAction.FADE).sendToAll();
            this.getPlayer().getEntityWorld().playSound(null, this.getPlayer().posX, this.getPlayer().posY, this.getPlayer().posZ,
                    SoundRegistry.getInstance().SOUND_FADE, SoundCategory.PLAYERS, 0.5F, 0.5F);
        }
        this.effectTimers.add(MagicEffectTimer.Fade(this));
        this.clearCharges();
    }

    @Override
    public void fizzle() {
        if(!this.getPlayer().getEntityWorld().isRemote) {
            Vec3d vec3d = this.getPlayer().getLookVec();
            Vec3d dir = new Vec3d(-vec3d.x, -vec3d.y, -vec3d.z);
            for(Element element : Element.values()) {
                int potency = this.getPotencyMap().getPotency(element);
                if(potency > 0) {
                    MagicDamageHandler.getInstance().dealDamage(this.getPlayer(), potency, this.getPlayer(), element, potency, dir);
                }
            }
            new MessageChargeAction(this.getPlayer(), EnumMagicChargeAction.FIZZLE).sendToAll();
            this.getPlayer().getEntityWorld().playSound(null, this.getPlayer().posX, this.getPlayer().posY, this.getPlayer().posZ,
                    SoundRegistry.getInstance().SOUND_FIZZLE, SoundCategory.PLAYERS, 0.5F, 0.5F);
        }
        this.effectTimers.add(MagicEffectTimer.Fizzle(this));
        this.clearCharges();
    }

    @Override
    public void addCharge(IMagicCharge charge) {
        if(charge != null) {
            this.chargeMap.get(charge.element()).add(charge);
            this.charges.add(charge);
            if(!getPlayer().getEntityWorld().isRemote) {
                new MessageAddCharge(this.getPlayer(), charge).sendToAll();
            }
            recalculateInstability(charge);
            this.getPotencyMap().addPotency(charge.element(), charge.potency());
        }
    }

    @Override
    public List<IMagicCharge> getCharges() {
        return ImmutableList.copyOf(charges);
    }

    @Override
    public List<IMagicCharge> getCharges(Element element) {
        return ImmutableList.copyOf(chargeMap.get(element));
    }

    @Override
    public PotencyMap getPotencyMap() {
        return this.potencyMap;
    }

    @Override
    public void clearCharges(){
        this.charges.clear();
        this.chargeMap.values().forEach(List::clear);
        this.resetInstability();
        this.potencyMap = new PotencyMap();
    }

    @Override
    public double getFizzleChance() {
        return this.pFizzle;
    }

    @Override
    public double getInstabilityX() {
        return this.instX;
    }

    @Override
    public double getInstabilityY() {
        return this.instY;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        this.getCharges().stream().forEach((charge) -> {
            NBTTagCompound chargeTag = new NBTTagCompound();
            chargeTag.setInteger(Names.NBT.ELEMENT, charge.element().ordinal());
            chargeTag.setInteger(Names.NBT.LEVEL, charge.potency());
            list.appendTag(chargeTag);
        });
        tag.setTag(Names.NBT.CHARGE, list);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.clearCharges();
        if(tag.hasKey(Names.NBT.CHARGE)) {
            NBTTagList list = tag.getTagList(Names.NBT.CHARGE, 10);
            for(int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound chargeTag = list.getCompoundTagAt(i);
                if(chargeTag.hasKey(Names.NBT.ELEMENT) && chargeTag.hasKey(Names.NBT.LEVEL)) {
                    Element element = Element.values()[chargeTag.getInteger(Names.NBT.ELEMENT)];
                    int level = chargeTag.getInteger(Names.NBT.LEVEL);
                    this.addCharge(new IMagicCharge() {
                        @Override
                        public Element element() {
                            return element;
                        }

                        @Override
                        public int potency() {
                            return level;
                        }
                    });
                }
            }
        }
    }

    private boolean fizzleCheck() {
        return this.getFizzleChance() > 0 && this.getPlayer().getRNG().nextDouble() <= getFizzleChance();
    }

    private Optional<ISpell> getSpell() {
        return SpellRegistry.getInstance().getSpell(getCharges().stream().map(IMagicCharge::element).collect(Collectors.toList()));
    }

    private void recalculateInstability(IMagicCharge charge) {
        this.instX = this.instX + charge.element().calculateX(charge.potency());
        this.instY = this.instY + charge.element().calculateY(charge.potency());
        this.instR = Math.sqrt(this.instX*this.instX + this.instY*this.instY);
        if(this.instR < 1) {
            //configuration of charges is too stable and fades
            this.fade();
        } else {
            //update instability angle
            this.instA = Math.atan2(this.instY, instX);
            this.instA = this.instA + (this.instA < 0 ? 2*Math.PI : 0);
            //determine limit line
            Pair<Element, Element> elements = Element.getElementsForAngle(this.instA);
            double x1 = this.calculateElementX(elements.getKey());
            double y1 = this.calculateElementY(elements.getKey());
            double x2 = this.calculateElementX(elements.getValue());
            double y2 = this.calculateElementY(elements.getValue());
            //determine limit point
            if(x2 == x1) {
                //2 elements are identical
                this.limX = x1;
                this.limY = y1;
            } else {
                double m1 = (y2 - y1) / (x2 - x1);
                double c1 = y1 - m1 * x1;
                if (this.instX == 0) {
                    this.limX = 0;
                    this.limY = c1;
                } else {
                    double m2 = this.instY / this.instX;
                    this.limX =  c1 / (m2 - m1);
                    this.limY = m2*this.limX;
                }
            }
            this.limA = Math.atan2(this.limY, limX);
            this.limA = this.limA + (this.limA < 0 ? 2*Math.PI : 0);
            this.limR = Math.sqrt(this.limX*this.limX + this.limY*this.limY);
            //determine fizzle chance
            this.pFizzle = instR > limR ? 1 - Math.exp((limR - instR)/ ModConfiguration.getInstance().getFizzleConstant()) : 0;
        }
    }

    private double calculateElementX(Element element) {
        return element.calculateX(((double) this.getProperties().getPlayerAdeptness(element)* Constants.CORE_TIERS*Constants.NOMINAL_ORBS)/Constants.MAX_LEVEL);
    }

    private double calculateElementY(Element element) {
        return element.calculateY(((double) this.getProperties().getPlayerAdeptness(element)*Constants.CORE_TIERS*Constants.NOMINAL_ORBS)/Constants.MAX_LEVEL);
    }

    private void addExperienceOnCast(boolean invoke) {
        if(this.instR > this.limR) {
            double amount = Constants.EXP_BASE*Constants.EXP_BASE*(1-Math.exp((limR - instR)/ ModConfiguration.getInstance().getExpConstant()));
            if(invoke) {
                amount = amount * ModConfiguration.getInstance().getExpComboMultiplier();
            }
            Pair<Element, Element> elements = Element.getElementsForAngle(this.instA);
            double angle1 = elements.getKey().getPolarAngle();
            double angle2 = elements.getValue().getPolarAngle();
            double da =  clampDeltaAngle(angle1 - this.instA);
            double delta = clampDeltaAngle(angle1 - angle2);
            this.getProperties().addExperience(elements.getKey(), (int) ((1 - da/delta)*amount));
            this.getProperties().addExperience(elements.getValue(), (int) ((da/delta)*amount));
        }
    }

    private double clampDeltaAngle(double angle) {
        if(angle > Math.PI) {
            angle = angle - 2*Math.PI;
        } else if(angle <= -Math.PI) {
            angle = angle + 2*Math.PI;
        }
        return angle;
    }

    private void resetInstability() {
        this.instX = 0;
        this.instY = 0;
        this.instA = 0;
        this.instR= 0;
        this.limX = 0;
        this.limY = 0;
        this.limA = 0;
        this.limR = 0;
        this.pFizzle = 0;
    }
}
