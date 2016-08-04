package com.teaminfinity.elementalinvocations.magic;

import com.teaminfinity.elementalinvocations.ElementalInvocations;
import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.IMagicCharge;
import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import com.teaminfinity.elementalinvocations.network.MessageAddCharge;
import com.teaminfinity.elementalinvocations.network.NetworkWrapper;
import com.teaminfinity.elementalinvocations.reference.Constants;
import com.teaminfinity.elementalinvocations.reference.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            this.chargeMap.get(element).clear();
        }
        this.charges.clear();
    }

    @Override
    public void addCharge(IMagicCharge charge) {
        if(charge != null) {
            this.chargeMap.get(charge.element()).add(charge);
            this.charges.add(charge);
            if(ElementalInvocations.proxy.getEffectiveSide() == Side.SERVER) {
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

    }

    private boolean fizzleCheck() {
        return false;
    }

    private void fizzle() {

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
