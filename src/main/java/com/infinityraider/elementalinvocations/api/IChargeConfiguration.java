package com.infinityraider.elementalinvocations.api;

import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public interface IChargeConfiguration {

    void updateTick();

    void invoke();

    void fade();

    void fizzle();

    void addCharge(IMagicCharge charge);

    List<IMagicCharge> getCharges();

    List<IMagicCharge> getCharges(Element element);

    IPotencyMap getPotencyMap();

    void clearCharges();

    double getFizzleChance();

    double getInstabilityX();

    double getInstabilityY();

    NBTTagCompound writeToNBT();

    void readFromNBT(NBTTagCompound tag);
}
