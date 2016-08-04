/*
 */
package com.teaminfinity.elementalinvocations.api.spells;

import net.minecraft.entity.player.EntityPlayer;

/**
 *
 * @author RlonRyan
 */
@FunctionalInterface
public interface ISpellRequirement {
	
	boolean isMet(EntityPlayer player);
	
}
