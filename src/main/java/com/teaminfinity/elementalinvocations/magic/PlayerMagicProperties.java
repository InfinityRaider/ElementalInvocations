package com.teaminfinity.elementalinvocations.magic;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.IMagicCharge;
import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerMagicProperties implements IPlayerMagicProperties {
    @Override
    public Element playerAffinity() {
        return null;
    }

    @Override
    public int playerAdeptness() {
        return 0;
    }

    @Override
    public void addCharge(IMagicCharge charge) {

    }

    @Override
    public int currentInstability() {
        return 0;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }
}
