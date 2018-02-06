package com.infinityraider.elementalinvocations.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public interface IPlayerMagicProperties {
    EntityPlayer getPlayer();

    void updateTick();

    void setPlayerAffinity(Element element);

    Element getPlayerAffinity();

    void setPlayerAdeptness(Element element, int level);

    int getPlayerAdeptness(Element element);

    double getInstabilityX();

    double getInstabilityY();

    double getFizzleChance();

    float getRed();

    float getBlue();

    float getGreen();

    void reset();

    void invoke();

    void fade();

    void fizzle();

    void addCharge(IMagicCharge charge);

    List<IMagicCharge> getCharges();

    List<IMagicCharge> getCharges(Element element);

    NBTTagCompound writeToNBT();

    void readFromNBT(NBTTagCompound tag);
}
