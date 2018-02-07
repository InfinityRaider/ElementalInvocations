package com.infinityraider.elementalinvocations.api;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Interface holding data related to the potency of a combination of charges
 */
public interface IPotencyMap {
    /**
     * Gets the total potency of all the elements combined
     * @return the total potency
     */
    int getTotalPotency();

    /**
     * Gets the potency of a certain element
     * @param element the element
     * @return the potency
     */
    int getPotency(Element element);

    /**
     * Gets the red color-value of the potency
     * @return the RGB red value (between 0.0F and 1.0F)
     */
    float getRed();

    /**
     * Gets the blue color-value of the potency
     * @return the RGB blue value (between 0.0F and 1.0F)
     */
    float getBlue();

    /**
     * Gets the green color-value of the potency
     * @return the RGB green value (between 0.0F and 1.0F)
     */
    float getGreen();

    /**
     * Serializes the current potency data to an NBT tag
     * @return an NBTTagCompound with the data of the potency data serialized
     */
    NBTTagCompound writeToNBT();

    /**
     * Deserializes data from an NBT tag
     * @param tag the NBTTagCompound holding the information
     * @return this
     */
    IPotencyMap readFromNBT(NBTTagCompound tag);
}
