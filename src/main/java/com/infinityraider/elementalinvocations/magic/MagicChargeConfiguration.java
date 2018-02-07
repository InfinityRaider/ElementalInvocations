package com.infinityraider.elementalinvocations.magic;

import com.google.common.collect.ImmutableList;
import com.infinityraider.elementalinvocations.api.*;
import com.infinityraider.elementalinvocations.api.spells.ISpell;
import com.infinityraider.elementalinvocations.entity.EntityMagicProjectile;
import com.infinityraider.elementalinvocations.handler.ConfigurationHandler;
import com.infinityraider.elementalinvocations.magic.generic.MagicEffect;
import com.infinityraider.elementalinvocations.magic.spell.SpellRegistry;
import com.infinityraider.elementalinvocations.network.MessageAddCharge;
import com.infinityraider.elementalinvocations.network.MessageInvoke;
import com.infinityraider.elementalinvocations.reference.Constants;
import javafx.util.Pair;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
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

    protected MagicChargeConfiguration(IPlayerMagicProperties properties) {
        this.props = properties;
        this.chargeMap = new HashMap<>();
        for(Element element : Element.values()) {
            this.chargeMap.put(element, new ArrayList<>());
        }
        this.charges = new ArrayList<>();
        this.clearCharges();
    }

    public IPlayerMagicProperties getProperties() {
        return this.props;
    }

    public EntityPlayer getPlayer() {
        return this.getProperties().getPlayer();
    }

    @Override
    public void updateTick() {
        if (!this.getCharges().isEmpty() && this.fizzleCheck()) {
            this.fizzle();
        }
    }

    @Override
    public void invoke() {
        if (getCharges().size() <= 0) {
            return;
        }
        if (!getPlayer().getEntityWorld().isRemote) {
            Optional<ISpell> spell = getSpell();
            spell.ifPresent(iSpell -> iSpell.invoke(getPlayer(), this.getPotencyMap()));
            //TODO: add experience
            if (!spell.isPresent()) {
                EntityMagicProjectile projectile = new EntityMagicProjectile(getPlayer(), this.getPotencyMap());
                getPlayer().getEntityWorld().spawnEntityInWorld(projectile);
            }
            new MessageInvoke(getPlayer(), false).sendToAll();
        }
        this.clearCharges();
    }

    @Override
    public void fade() {
        this.clearCharges();
    }

    @Override
    public void fizzle() {
        if(!getPlayer().getEntityWorld().isRemote) {
            Vec3d vec3d = getPlayer().getLookVec();
            new MagicEffect(getPlayer(), getPlayer(), new Vec3d(-vec3d.xCoord, -vec3d.yCoord, -vec3d.zCoord), this.getPotencyMap()).apply();
            new MessageInvoke(getPlayer(), true).sendToAll();
        }
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
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }

    private boolean fizzleCheck() {
        return this.getPlayer().getRNG().nextDouble() <= getFizzleChance();
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
            this.limR = Math.sqrt(this.limX*this.limX + this.limX*this.limY);
            //determine fizzle chance
            this.pFizzle = instR > limR ? 1 - Math.exp((limR - instR)/ConfigurationHandler.getInstance().fizzleConstant) : 0;
        }
    }

    private double calculateElementX(Element element) {
        return element.calculateX(((double) this.getProperties().getPlayerAdeptness(element)* Constants.CORE_TIERS*Constants.NOMINAL_ORBS)/Constants.MAX_LEVEL);
    }

    private double calculateElementY(Element element) {
        return element.calculateY(((double) this.getProperties().getPlayerAdeptness(element)*Constants.CORE_TIERS*Constants.NOMINAL_ORBS)/Constants.MAX_LEVEL);
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
