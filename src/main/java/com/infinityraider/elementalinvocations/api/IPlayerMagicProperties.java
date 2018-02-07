package com.infinityraider.elementalinvocations.api;

import jline.internal.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerMagicProperties {
    EntityPlayer getPlayer();

    void updateTick();

    void setPlayerAffinity(@Nullable Element element);

    Element getPlayerAffinity();

    void setPlayerAdeptness(Element element, int level);

    int getPlayerAdeptness(Element element);

    IChargeConfiguration getChargeConfiguration();

    void reset();

    NBTTagCompound writeToNBT();

    void readFromNBT(NBTTagCompound tag);
}
