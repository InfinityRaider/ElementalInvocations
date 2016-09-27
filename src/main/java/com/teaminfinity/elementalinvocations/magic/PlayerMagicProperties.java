package com.teaminfinity.elementalinvocations.magic;

import com.infinityraider.infinitylib.network.NetworkWrapper;
import com.infinityraider.infinitylib.utility.LogHelper;
import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.IMagicCharge;
import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.api.spells.ISpell;
import com.teaminfinity.elementalinvocations.entity.EntityMagicProjectile;
import com.teaminfinity.elementalinvocations.handler.ConfigurationHandler;
import com.teaminfinity.elementalinvocations.magic.generic.MagicEffect;
import com.teaminfinity.elementalinvocations.magic.spell.SpellRegistry;
import com.teaminfinity.elementalinvocations.network.MessageAddCharge;
import com.teaminfinity.elementalinvocations.network.MessageInvoke;
import com.teaminfinity.elementalinvocations.network.MessageSyncMagicProperties;
import com.teaminfinity.elementalinvocations.reference.Constants;
import com.teaminfinity.elementalinvocations.reference.Names;
import com.teaminfinity.elementalinvocations.reference.UniqueIds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class PlayerMagicProperties implements IPlayerMagicProperties {
    /* player instance */
    private EntityPlayer player;

    /* Persistent fields */
    private Element affinity;
    private int level;
    private int experience;

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
    }

    @Override
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
            NetworkWrapper.getInstance().sendToAll(new MessageSyncMagicProperties(player, this.writeToNBT()));
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
        LogHelper.debug("Set player affinity to " + element == null ? "NULL" : (element.getTextFormat() + element.name()));
    }

    @Override
    public Element getPlayerAffinity() {
        return this.affinity;
    }

    @Override
    public void setPlayerAdeptness(int level) {
        this.level = Math.max(0, Math.min(level, Constants.MAX_LEVEL));
        this.experience = 0;
        this.needsSync = true;
    }

    @Override
    public int getPlayerAdeptness() {
        return this.level;
    }

    @Override
    public void addExperience(int amount) {
        this.experience = experience + amount * ConfigurationHandler.getInstance().experienceMultiplier;
        int nextLevel = this.experienceToLevelUp();
        if(this.experience >= nextLevel && this.getPlayerAdeptness() < Constants.MAX_LEVEL) {
            int exp = this.experience - nextLevel;
            this.setPlayerAdeptness(this.getPlayerAdeptness() + 1);
            this.experience = exp;
        }
        this.needsSync = true;
    }

    private int experienceToLevelUp() {
        return calculatePowerRecursively(ConfigurationHandler.getInstance().levelingSpeed, this.getPlayerAdeptness() + 1);
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
        this.level = 0;
        this.experience = 0;
        this.currentInstability = 0;
        for(Element element : Element.values()) {
            this.chargeMap.get(element).clear();
        }
        this.charges.clear();
        this.needsSync = true;
    }

    @Override
    public void invoke() {
        if (getCharges().size() <= 0 || this.getPlayerAffinity() == null) {
            return;
        }
        ISpell spell = getSpell();
        int[] potencies = getPotencyArray(false);
        this.addExperience(potencies[this.getPlayerAffinity().ordinal()] * charges.size());
        if(spell != null) {
            spell.invoke(getPlayer(), potencies);
        }
        if (!getPlayer().getEntityWorld().isRemote) {
            if (spell == null) {
                EntityMagicProjectile projectile = new EntityMagicProjectile(getPlayer(), getPotencyArray(false));
                getPlayer().getEntityWorld().spawnEntityInWorld(projectile);
            }
            NetworkWrapper.getInstance().sendToAll(new MessageInvoke(getPlayer(), false));
        }
        this.getCharges().clear();
        this.currentInstability = 0;
    }

    @Override
    public void fizzle() {
        if(!getPlayer().getEntityWorld().isRemote) {
            Vec3d vec3d = getPlayer().getLookVec();
            new MagicEffect(getPlayer(), getPlayer(), new Vec3d(-vec3d.xCoord, -vec3d.yCoord, -vec3d.zCoord), getPotencyArray(true)).apply();
            NetworkWrapper.getInstance().sendToAll(new MessageInvoke(getPlayer(), true));
        }
        this.getCharges().clear();
    }

    @Nullable
    private ISpell getSpell() {
        Optional<ISpell> spell = SpellRegistry.getInstance().getSpell(getCharges().stream().map(IMagicCharge::element).collect(Collectors.toList()));
        return spell.isPresent() ? spell.get() : null;
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
                NetworkWrapper.getInstance().sendToAll(new MessageAddCharge(this.getPlayer(), charge));
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
        tag.setInteger(Names.NBT.EXPERIENCE, this.experience);
        tag.setInteger(Names.NBT.LEVEL, this.level);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.affinity = tag.hasKey(Names.NBT.ELEMENT) ? Element.values()[tag.getInteger(Names.NBT.ELEMENT)] : null;
        this.experience = tag.hasKey(Names.NBT.EXPERIENCE) ? tag.getInteger(Names.NBT.EXPERIENCE) : 0;
        this.level = tag.hasKey(Names.NBT.LEVEL) ? tag.getInteger(Names.NBT.LEVEL) : 0;
        this.needsSync = true;
    }
}
