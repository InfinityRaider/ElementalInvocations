package com.infinityraider.elementalinvocations.magic;

import com.infinityraider.elementalinvocations.api.IChargeConfiguration;
import com.infinityraider.infinitylib.utility.ISerializable;
import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPlayerMagicProperties;
import com.infinityraider.elementalinvocations.network.MessageSyncMagicProperties;
import com.infinityraider.elementalinvocations.reference.Constants;
import com.infinityraider.elementalinvocations.reference.Names;
import jline.internal.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

public class PlayerMagicProperties implements IPlayerMagicProperties, ISerializable {
    /* player instance */
    private EntityPlayer player;
    /* Charge configuration */
    private final IChargeConfiguration chargeConfiguration;

    /* Persistent fields */
    /** The affinity of the player */
    private Element affinity;
    /** The current level of each of the elements */
    private Map<Element, Integer> levels;
    /** The current experience for each of the elements */
    private Map<Element, Integer> experience;

    /* Non-persistent fields */
    /** Flag to check if the server needs to sync to the clients */
    private boolean needsSync;

    public PlayerMagicProperties() {
        this.levels = new HashMap<>();
        this.experience = new HashMap<>();
        this.chargeConfiguration = new MagicChargeConfiguration(this);
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
        }
        this.getChargeConfiguration().updateTick();
    }

    @Override
    public void setPlayerAffinity(@Nullable Element element) {
        this.affinity = element;
        this.needsSync = true;
        ElementalInvocations.instance.getLogger().debug("Set player affinity to " + (element == null? "NULL" : (element.getTextFormat() + element.name())));
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
    public IChargeConfiguration getChargeConfiguration() {
        return this.chargeConfiguration;
    }

    @Override
    public void reset() {
        this.affinity = null;
        this.getChargeConfiguration().clearCharges();
        for(Element element : Element.values()) {
            this.levels.put(element, Constants.MIN_LEVEL);
            this.experience.put(element, 0);
        }
        this.needsSync = true;
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
        tag.setTag(Names.NBT.CHARGE, this.getChargeConfiguration().writeToNBT());
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        this.affinity = tag.hasKey(Names.NBT.ELEMENT) ? Element.values()[tag.getInteger(Names.NBT.ELEMENT)] : null;
        for (Element element : Element.values()) {
            this.experience.put(element, tag.hasKey(Names.NBT.EXPERIENCE + "_" + element.ordinal()) ? tag.getInteger(Names.NBT.EXPERIENCE + "_" + element.ordinal()) : 0);
            this.levels.put(element, tag.hasKey(Names.NBT.LEVEL + "_" + element.ordinal()) ? tag.getInteger(Names.NBT.LEVEL + "_" + element.ordinal()) : 1);
        }
        if(tag.hasKey(Names.NBT.CHARGE)) {
            this.getChargeConfiguration().readFromNBT(tag.getCompoundTag(Names.NBT.CHARGE));
        }
        this.needsSync = true;
    }
}
