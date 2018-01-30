package com.infinityraider.elementalinvocations.magic;

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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerMagicProperties implements IPlayerMagicProperties, ISerializable {
    /* player instance */
    private EntityPlayer player;

    /* Persistent fields */
    private Element affinity;
    private Map<Element, Integer> levels;
    private Map<Element, Integer> experience;

    /* Non-persistent fields */
    private int currentInstability;
    private Map<Element, List<IMagicCharge>> chargeMap;
    private List<IMagicCharge> charges;
    private boolean needsSync;

    public PlayerMagicProperties() {
        this.chargeMap = new HashMap<>();
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
        if(this.needsSync && !this.player.getEntityWorld().isRemote) {
            new MessageSyncMagicProperties(player, this.writeToNBT()).sendToAll();
            this.needsSync = false;
        }
        if(this.getCharges().isEmpty()) {
            this.currentInstability = Math.max(0, this.currentInstability - 1);
        }
    }

    @Override
    public void setPlayerAffinity(Element element) {
        this.affinity = element;
        if(element == null) {
            this.getCharges().clear();
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
        this.levels.put(element, Math.max(0, Math.min(level, Constants.MAX_LEVEL)));
        this.experience.put(element, 0);
        this.needsSync = true;
    }

    @Override
    public int getPlayerAdeptness(Element element) {
        return this.levels.get(element);
    }

    @Override
    public void addExperience(Element element, int amount) {
        if(this.getPlayerAdeptness(element) < Constants.MAX_LEVEL) {
            int exp = this.experience.get(element) + amount * ConfigurationHandler.getInstance().experienceMultiplier;
            int nextLevel = this.experienceToLevelUp(element);
            if (exp >= nextLevel) {
                exp = this.experience.get(element) - nextLevel;
                this.setPlayerAdeptness(element, this.getPlayerAdeptness(element) + 1);
            }
            this.experience.put(element, exp);
            this.needsSync = true;
        }
    }

    private int experienceToLevelUp(Element element) {
        return calculatePowerRecursively(ConfigurationHandler.getInstance().levelingSpeed, this.getPlayerAdeptness(element) + 1);
    }

    private int calculatePowerRecursively(int base, int exponent) {
        if(base == 1 || exponent <= 0) {
            return 1;
        }
        return base * calculatePowerRecursively(base, exponent - 1);
    }

    @Override
    public void reset() {
        this.affinity = null;
        this.currentInstability = 0;
        for(Element element : Element.values()) {
            this.chargeMap.get(element).clear();
            this.levels.put(element, 1);
            this.experience.put(element, 0);
        }
        this.charges.clear();
        this.needsSync = true;
    }

    @Override
    public void invoke() {
        if (getCharges().size() <= 0 || this.getPlayerAffinity() == null) {
            return;
        }
        Optional<ISpell> spell = getSpell();
        getCharges().stream().forEach(charge -> this.addExperience(charge.element(), charge.level()*charges.size()));
        if(spell.isPresent()) {
            spell.get().invoke(getPlayer(), getPotencyArray(false));
        }
        if (!getPlayer().getEntityWorld().isRemote) {
            if (!spell.isPresent()) {
                EntityMagicProjectile projectile = new EntityMagicProjectile(getPlayer(), getPotencyArray(false));
                getPlayer().getEntityWorld().spawnEntityInWorld(projectile);
            }
            new MessageInvoke(getPlayer(), false).sendToAll();
        }
        this.getCharges().clear();
        this.currentInstability = 0;
    }

    @Override
    public void fizzle() {
        if(!getPlayer().getEntityWorld().isRemote) {
            Vec3d vec3d = getPlayer().getLookVec();
            new MagicEffect(getPlayer(), getPlayer(), new Vec3d(-vec3d.xCoord, -vec3d.yCoord, -vec3d.zCoord), getPotencyArray(true)).apply();
            new MessageInvoke(getPlayer(), true).sendToAll();
        }
        this.getCharges().clear();
    }

    private Optional<ISpell> getSpell() {
        return SpellRegistry.getInstance().getSpell(getCharges().stream().map(IMagicCharge::element).collect(Collectors.toList()));
    }

    private int[] getPotencyArray(boolean fizzle) {
        int[] potencies = new int[Element.values().length];
        for(IMagicCharge charge : this.charges) {
            float pre = 1.0F;
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
        if(charge != null && getPlayerAffinity() != null) {
            this.chargeMap.get(charge.element()).add(charge);
            this.charges.add(charge);
            if(!getPlayer().getEntityWorld().isRemote) {
                new MessageAddCharge(this.getPlayer(), charge).sendToAll();
                recalculateInstability(charge);
                if(fizzleCheck()) {
                    fizzle();
                }
            }
        }
    }

    @Override
    public List<IMagicCharge> getCharges() {
        return charges;
    }

    @Override
    public List<IMagicCharge> getCharges(Element element) {
        return chargeMap.get(element);
    }

    private void recalculateInstability(IMagicCharge charge) {
        if(charge.element() == this.getPlayerAffinity()) {
            this.currentInstability = this.currentInstability() + charge.level();
        } else if(charge.element().getOpposite() == this.getPlayerAffinity()) {
            this.currentInstability = this.currentInstability() + 3*charge.level();
        } else {
            this.currentInstability = this.currentInstability() + 2*charge.level();
        }
    }

    private boolean fizzleCheck() {
        if(player.getUniqueID().equals(UniqueIds.ARRRRIK)
                || player.getUniqueID().equals(UniqueIds.INFINITYRAIDER)
                || player.getUniqueID().equals(UniqueIds.RLONRYAN)) {
            return false;
        }
        return player.getRNG().nextDouble() < getFizzleChance();
    }

    private double getFizzleChance() {
        int max = this.getMaxInstability();
        if(this.currentInstability <= max) {
            return 0;
        }
        return  1.0 - Math.exp(-0.1*(this.currentInstability - max));
    }

    private int getMaxInstability() {
        return this.level*7 + 1;
    }

    @Override
    public int currentInstability() {
        return this.currentInstability;
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
