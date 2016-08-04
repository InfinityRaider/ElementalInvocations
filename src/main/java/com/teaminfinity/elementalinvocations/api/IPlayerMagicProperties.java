package com.teaminfinity.elementalinvocations.api;

import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerMagicProperties {
    void setPlayerAffinity(Element element);

    Element getPlayerAffinity();

    void setPlayerAdeptness(int level);

    int getPlayerAdeptness();

    void reset();

    void addCharge(IMagicCharge charge);

    int currentInstability();

    NBTTagCompound writeToNBT();

    void readFromNBT(NBTTagCompound tag);
}
