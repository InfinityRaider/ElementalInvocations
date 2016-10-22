package com.teaminfinity.elementalinvocations.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public interface IPlayerMagicProperties {
    EntityPlayer getPlayer();

    void updateTick();

    void setPlayerAffinity(Element element);

    Element getPlayerAffinity();

    void setPlayerAdeptness(int level);

    int getPlayerAdeptness();

    void addExperience(int amount);

    void reset();

    void invoke();

    void fizzle();

    void addCharge(IMagicCharge charge);

    List<IMagicCharge> getCharges();

    List<IMagicCharge> getCharges(Element element);

    int currentInstability();

    NBTTagCompound writeToNBT();

    void readFromNBT(NBTTagCompound tag);
}
