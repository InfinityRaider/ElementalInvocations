package com.infinityraider.elementalinvocations.api.souls;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Interface representing the souls collected by a player
 */
public interface ISoulCollection {
    /**
     * @return the player this soul collection belongs to
     */
	EntityPlayer getPlayer();

    /**
     * Adds a soul to the collection
     * @param soul the ISoul to add
     */
	void addSoul(ISoul soul);

    /**
     * Removes a soul from the collection
     * @return the removed soul
     */
	ISoul removeSoul();

    /**
     * Removes all souls from the collection
     * @return a list containing all removed souls
     */
	List<ISoul> releaseSouls();

    /**
     * Gets the number of souls in this collection
     * @return soul count
     */
	int getSoulCount();

    /**
     * Serializes the soul collection data to an NBT tag
     * @return an NBTTagCompound containing the necessary data of this soul collection
     */
	NBTTagCompound writeToNBT();

    /**
     * Deserializes the soul collection data from an NBT tag
     * @param tag an NBTTagCompound containing the necessary data of this soul collection
     */
	void readFromNBT(NBTTagCompound tag);

}
