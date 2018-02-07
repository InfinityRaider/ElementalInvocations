package com.infinityraider.elementalinvocations.api;

import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

/**
 * Interface representing the charges currently conjured by the player
 */
public interface IChargeConfiguration {
    /**
     * Automatically called each tick
     */
    void updateTick();

    /**
     * Invokes a spell from the currently conjured charges (normally happens when the player presses the invoke button)
     * Clears the charges in the process
     */
    void invoke();

    /**
     * Makes the charges fade away (normally happens when the player conjures a charge configuration which is too stable)
     * Clears the charges in the process
     */
    void fade();

    /**
     * Makes the charges fizzle (normally happens when the player keeps an unstable charge configuration for too long)
     * Clears the charges in the process
     */
    void fizzle();

    /**
     * Adds a new charge the the current configuration
     * @param charge the charge to add
     */
    void addCharge(IMagicCharge charge);

    /**
     * Gets the currently conjured charges
     * @return an immutable List holding all the charges
     */
    List<IMagicCharge> getCharges();

    /**
     * Gets the currently conjured charges for an element
     * @param element the element
     * @return an immutable List holding all the charges of the given element
     */
    List<IMagicCharge> getCharges(Element element);

    /**
     * Gets the potency map holding the potency state of the current configuration
     * @return the IPotencyMap object corresponding to the current configuration
     */
    IPotencyMap getPotencyMap();

    /**
     * Clears the currently conjured charges
     */
    void clearCharges();

    /**
     * Gets the fizzle chance of the currently conjured charge configuration
     * This chance represents the odds of a fizzle happening each tick
     *  - p = 0: the configuration is not sufficiently unstable and will never fizzle
     *  - p > 0: the configuration is too unstable for the player and will eventually fizzle
     * @return the fizzle chance
     */
    double getFizzleChance();

    /**
     * Gets the x-coordinate of the currently conjured charge configuration in  the polar stability diagram
     * @return the x-coordinate
     */
    double getInstabilityX();

    /**
     * Gets the y-coordinate of the currently conjured charge configuration in  the polar stability diagram
     * @return the y-coordinate
     */
    double getInstabilityY();

    /**
     * Serializes the current charge configuration to an NBT tag
     * @return an NBTTagCompound with the data of the charge configuration serialized
     */
    NBTTagCompound writeToNBT();

    /**
     * Deserializes data from an NBT tag
     * @param tag the NBTTagCompound holding the information
     */
    void readFromNBT(NBTTagCompound tag);
}
