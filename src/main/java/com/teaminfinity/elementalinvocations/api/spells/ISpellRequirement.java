/*
 */
package com.teaminfinity.elementalinvocations.api.spells;

import net.minecraft.entity.player.EntityPlayer;

/**
 *
 */
@FunctionalInterface
public interface ISpellRequirement {
	
	boolean isMet(EntityPlayer player);
	
}
