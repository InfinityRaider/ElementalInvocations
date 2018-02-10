package com.infinityraider.elementalinvocations.api;

import jline.internal.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Interface representing the magic properties of the player, implemented as a capability
 */
public interface IPlayerMagicProperties {
    /**
     * @return the player to which these properties belong
     */
    EntityPlayer getPlayer();

    /**
     * Called each tick automatically
     */
    void updateTick();

    /**
     * Sets the affinity of the player to a certain element
     * @param element the element
     */
    void setPlayerAffinity(@Nullable Element element);

    /**
     * Gets the element to which the player currently has its affinity
     * @return the affinity
     */
    Element getPlayerAffinity();

    /**
     * Sets the player's magic potency for a certain element
     * @param element the element
     * @param level the potency
     */
    void setPlayerAdeptness(Element element, int level);

    /**
     * Gets the player's current magic potency for a certain element
     * @param element the element
     * @return the potency
     */
    int getPlayerAdeptness(Element element);

    /**
     * Gets the amount of experience required to gain a level
     * When the player's experience for an element reaches this amount,
     * the player's adeptness for that element increases by one level.
     * @param element the element
     * @return the amount required to level up
     */
    int getExperienceToLevelUp(Element element);

    /**
     * Adds an amount of experience for a certain element
     * @param element the element
     * @param amount the amount
     */
    void addExperience(Element element, int amount);

    /**
     * Gets the experience a player has for a certain element
     * @param element the element
     * @return the experience
     */
    int getExperience(Element element);

    /**
     * Gets the configuration of the currently conjured charges
     * @return IChargeConfiguration object
     */
    IChargeConfiguration getChargeConfiguration();

    /**
     * Resets the magic properties:
     *  - sets the affinity to null
     *  - resets the magic levels and experience to 0
     *  - clears the currently conjured charges
     */
    void reset();

    /**
     * Serializes the properties to an NBT tag
     * @return an NBTTagCompound with the data of the properties serialized
     */
    NBTTagCompound writeToNBT();

    /**
     * Deserializes data from an NBT tag
     * @param tag the NBTTagCompound holding the information
     */
    void readFromNBT(NBTTagCompound tag);
}
