package com.teaminfinity.elementalinvocations.api.souls;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public interface ISoulCollection {

	EntityPlayer getPlayer();

	void addSoul(ISoul soul);

	ISoul removeSoul();

	List<ISoul> releaseSouls();

	int getSoulCount();

	NBTTagCompound writeToNBT();

	void readFromNBT(NBTTagCompound tag);

}
