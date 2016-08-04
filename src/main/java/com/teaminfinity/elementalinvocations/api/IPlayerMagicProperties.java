package com.teaminfinity.elementalinvocations.api;

import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerMagicProperties {
    Element playerAffinity();

    int playerAdeptness();

    void addCharge(IMagicCharge charge);

    int currentInstability();

    NBTTagCompound writeToNBT();

    void readFromNBT(NBTTagCompound tag);
}
