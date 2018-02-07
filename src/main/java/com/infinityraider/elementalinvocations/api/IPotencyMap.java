package com.infinityraider.elementalinvocations.api;

import net.minecraft.nbt.NBTTagCompound;

public interface IPotencyMap {
    int getTotalPotency();

    int getPotency(Element element);

    float getRed();

    float getBlue();

    float getGreen();

    NBTTagCompound writeToNBT();

    IPotencyMap readFromNBT(NBTTagCompound tag);
}
