package com.infinityraider.elementalinvocations.magic;

import com.google.common.collect.ImmutableList;
import com.infinityraider.elementalinvocations.api.spells.ISpell;
import com.infinityraider.elementalinvocations.network.MessageAddCharge;
import com.infinityraider.infinitylib.utility.ISerializable;
import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IMagicCharge;
import com.infinityraider.elementalinvocations.api.IPlayerMagicProperties;
import com.infinityraider.elementalinvocations.entity.EntityMagicProjectile;
import com.infinityraider.elementalinvocations.handler.ConfigurationHandler;
import com.infinityraider.elementalinvocations.magic.generic.MagicEffect;
import com.infinityraider.elementalinvocations.magic.spell.SpellRegistry;
import com.infinityraider.elementalinvocations.network.MessageInvoke;
import com.infinityraider.elementalinvocations.network.MessageSyncMagicProperties;
import com.infinityraider.elementalinvocations.reference.Constants;
import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.infinitylib.reference.UniqueIds;
import javafx.util.Pair;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerMagicProperties implements IPlayerMagicProperties, ISerializable {
    /* player instance */
    private EntityPlayer player;

    /* Persistent fields */
    /** The affinity of the player */
    private Element affinity;
    /** The current level of each of the elements */
    private Map<Element, Integer> levels;
    /** The current experience for each of the elements */
    private Map<Element, Integer> experience;

    /* Non-persistent fields */
    /** Coordinates of the instability point in the polar field */
    private double instX;   //x-coordinate
    private double instY;   //y-coordinate
    private double instA;   //polar angle
    private double instR;   //polar radius
    /** Coordinates of the level limit point in the polar field */
    private double limX;    //x-coordinate
    private double limY;    //y-coordinate
    private double limA;    //polar angle
    private double limR;    //polar radius
    /** Color values */
    private float red;
    private float green;
    private float blue;
    private int count;
    /** Current fizzle chance */
    private double pFizzle;
    /** Currently invoked charges */
    private Map<Element, List<IMagicCharge>> chargeMap;
    private List<IMagicCharge> charges;
    /** Flag to check if the server needs to sync to the clients */
    private boolean needsSync;

    public PlayerMagicProperties() {
        this.chargeMap = new HashMap<>();
        this.levels = new HashMap<>();
        this.experience = new HashMap<>();
        for(Element element : Element.values()) {
            this.chargeMap.put(element, new ArrayList<>());
        }
        this.charges = new ArrayList<>();
        this.reset();
        this.needsSync = false;
    }

    public PlayerMagicProperties setPlayer(EntityPlayer player) {
        this.player = player;
        return this;
    }

    @Override
    public EntityPlayer getPlayer() {
        return this.player;
    }

    @Override
    public void updateTick() {
        if(!this.player.getEntityWorld().isRemote) {
            if (this.needsSync) {
                new MessageSyncMagicProperties(player, this.writeToNBT()).sendToAll();
                this.needsSync = false;
            }
            if (!this.getCharges().isEmpty() && this.fizzleCheck()) {
                this.fizzle();
            }
        }
    }

    @Override
    public void setPlayerAffinity(Element element) {
        this.affinity = element;
        if(element == null) {
            this.clearCharges();
        }
        this.needsSync = true;
        ElementalInvocations.instance.getLogger().debug("Set player affinity to " + element == null ? "NULL" : (element.getTextFormat() + element.name()));
    }

    @Override
    public Element getPlayerAffinity() {
        return this.affinity;
    }

    @Override
    public void setPlayerAdeptness(Element element, int level) {
        this.levels.put(element, Math.max(Constants.MIN_LEVEL, Math.min(level, Constants.MAX_LEVEL)));
        this.experience.put(element, 0);
        this.needsSync = true;
    }

    @Override
    public int getPlayerAdeptness(Element element) {
        return this.levels.get(element);
    }

    @Override
    public double getInstabilityX() {
        return this.instX;
    }

    @Override
    public double getInstabilityY() {
        return this.instY;
    }

    private boolean fizzleCheck() {
        if(player.getUniqueID().equals(UniqueIds.INFINITYRAIDER)) {
            return false;
        }
        return player.getRNG().nextDouble() <= getFizzleChance();
    }

    @Override
    public double getFizzleChance() {
        return this.pFizzle;
    }

    @Override
    public float getRed() {
        return this.count == 0 ? 1.0F : this.red;
    }

    @Override
    public float getBlue() {
        return this.count == 0 ? 1.0F : this.blue;
    }

    @Override
    public float getGreen() {
        return this.count == 0 ? 1.0F : this.green;
    }

    @Override
    public void reset() {
        this.affinity = null;
        this.clearCharges();
        for(Element element : Element.values()) {
            this.levels.put(element, Constants.MIN_LEVEL);
            this.experience.put(element, 0);
        }
        this.needsSync = true;
    }

    @Override
    public void invoke() {
        if (getCharges().size() <= 0) {
            return;
        }
        if (!getPlayer().getEntityWorld().isRemote) {
            Optional<ISpell> spell = getSpell();
            if(spell.isPresent()) {
                spell.get().invoke(getPlayer(), getPotencyArray(false));
            }
            //TODO: add experience
            if (!spell.isPresent()) {
                EntityMagicProjectile projectile = new EntityMagicProjectile(getPlayer(), getPotencyArray(false));
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
            new MagicEffect(getPlayer(), getPlayer(), new Vec3d(-vec3d.xCoord, -vec3d.yCoord, -vec3d.zCoord), getPotencyArray(true)).apply();
            new MessageInvoke(getPlayer(), true).sendToAll();
        }
        this.clearCharges();
    }

    private Optional<ISpell> getSpell() {
        return SpellRegistry.getInstance().getSpell(getCharges().stream().map(IMagicCharge::element).collect(Collectors.toList()));
    }

    private int[] getPotencyArray(boolean fizzle) {
        int[] potencies = new int[Element.values().length];
        for(IMagicCharge charge : this.charges) {
            double pre = 1.0;
            if(charge.element() == this.getPlayerAffinity()) {
                pre = pre + ConfigurationHandler.getInstance().affinityBonus;
            } else if(charge.element() == this.getPlayerAffinity().getOpposite()) {
                pre = pre - ConfigurationHandler.getInstance().affinityBonus;
            }
            potencies[charge.element().ordinal()] += (int) ((pre * charge.level()) / (fizzle ? 2.0F : 1.0F));
        }

        return potencies;
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
            int n = this.count + charge.level();
            this.red = (this.red*255*this.count + charge.element().getRed()*charge.level())/(255*n);
            this.green = (this.green*255*this.count + charge.element().getGreen()*charge.level())/(255*n);
            this.blue = (this.blue*255*this.count + charge.element().getBlue()*charge.level())/(255*n);
            this.count = n;
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

    private void clearCharges(){
        this.charges.clear();
        this.chargeMap.values().forEach(List::clear);
        this.resetInstability();
        this.red = 0;
        this.green = 0;
        this.blue = 0;
        this.count = 0;
    }

    private void recalculateInstability(IMagicCharge charge) {
        this.instX = this.instX + charge.element().calculateX(charge.level());
        this.instY = this.instY + charge.element().calculateY(charge.level());
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
        return element.calculateX(((double) this.levels.get(element)*Constants.CORE_TIERS*Constants.NOMINAL_ORBS)/Constants.MAX_LEVEL);
    }

    private double calculateElementY(Element element) {
        return element.calculateY(((double) this.levels.get(element)*Constants.CORE_TIERS*Constants.NOMINAL_ORBS)/Constants.MAX_LEVEL);
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        if(this.affinity != null) {
            tag.setInteger(Names.NBT.ELEMENT, this.affinity.ordinal());
        }
        for(Element element : Element.values()) {
            tag.setInteger(Names.NBT.EXPERIENCE + "_" + element.ordinal(), this.experience.get(element));
            tag.setInteger(Names.NBT.LEVEL + "_" + element.ordinal(), this.levels.get(element));
        }
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.affinity = tag.hasKey(Names.NBT.ELEMENT) ? Element.values()[tag.getInteger(Names.NBT.ELEMENT)] : null;
        for (Element element : Element.values()) {
            this.experience.put(element, tag.hasKey(Names.NBT.EXPERIENCE + "_" + element.ordinal()) ? tag.getInteger(Names.NBT.EXPERIENCE + "_" + element.ordinal()) : 0);
            this.levels.put(element, tag.hasKey(Names.NBT.LEVEL + "_" + element.ordinal()) ? tag.getInteger(Names.NBT.LEVEL + "_" + element.ordinal()) : 1);
        }
        this.needsSync = true;
    }
}
