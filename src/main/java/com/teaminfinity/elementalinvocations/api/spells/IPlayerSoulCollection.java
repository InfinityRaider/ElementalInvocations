package com.teaminfinity.elementalinvocations.api.spells;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerSoulCollection {
    EntityPlayer getPlayer();

    IPlayerSoulCollection setPlayer(EntityPlayer player);

    IPlayerSoulCollection addSoul();

    IPlayerSoulCollection removeSoul();

    IPlayerSoulCollection releaseSouls();

    int getSoulCount();

    NBTTagCompound writeToNBT();

    void readFromNBT(NBTTagCompound tag);
}
