package com.teaminfinity.elementalinvocations.magic;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.IMagicCharge;
import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.reference.Constants;
import com.teaminfinity.elementalinvocations.reference.Names;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerMagicProperties implements IPlayerMagicProperties {
    /* Persistent fields */
    private Element affinity;
    private int level;
    private int experience;

    /* Non-persistent fields */
    private int currentInstability;
    private int chargeCount;
    private Map<Element, List<IMagicCharge>> charges;

    public PlayerMagicProperties() {
        this.charges = new HashMap<>();
        for(Element element : Element.values()) {
            this.charges.put(element, new ArrayList<>());
        }
    }

    @Override
    public void setPlayerAffinity(Element element) {
        this.affinity = element;
    }

    @Override
    public Element getPlayerAffinity() {
        return this.affinity;
    }

    @Override
    public void setPlayerAdeptness(int level) {
        this.level = Math.min(level, Constants.MAX_LEVEL);
        this.experience = 0;
    }

    @Override
    public int getPlayerAdeptness() {
        return this.level;
    }

    @Override
    public void reset() {
        this.affinity = null;
        this.level = 0;
        this.experience = 0;
        this.currentInstability = 0;
        for(Element element : Element.values()) {
            this.charges.get(element).clear();
        }
        this.chargeCount = 0;
    }

    @Override
    public void addCharge(IMagicCharge charge) {
        if(charge != null) {
            this.chargeCount++;
            this.charges.get(charge.element()).add(charge);
            recalculateInstability(charge);
        }
    }

    private void recalculateInstability(IMagicCharge charge) {

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
    }
}
