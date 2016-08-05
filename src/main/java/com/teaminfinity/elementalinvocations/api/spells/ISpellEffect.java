/*
 */
package com.teaminfinity.elementalinvocations.api.spells;

import net.minecraft.entity.player.EntityPlayer;

@FunctionalInterface
public interface ISpellEffect {
	
	void apply(EntityPlayer caster, int[] potencies);
	
}
