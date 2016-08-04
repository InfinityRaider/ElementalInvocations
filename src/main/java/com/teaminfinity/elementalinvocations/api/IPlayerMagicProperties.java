package com.teaminfinity.elementalinvocations.api;

import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public interface IPlayerMagicProperties {
    void setPlayerAffinity(Element element);

    Element getPlayerAffinity();

    void setPlayerAdeptness(int level);

    int getPlayerAdeptness();

    void reset();

    void addCharge(IMagicCharge charge);

    List<IMagicCharge> getCharges();

    List<IMagicCharge> getCharges(Element element);

    int currentInstability();

    NBTTagCompound writeToNBT();

    void readFromNBT(NBTTagCompound tag);
}
