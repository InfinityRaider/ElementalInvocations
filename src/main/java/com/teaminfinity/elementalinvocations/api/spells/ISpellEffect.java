/*
 */
package com.teaminfinity.elementalinvocations.api.spells;

import net.minecraft.entity.player.EntityPlayer;

@FunctionalInterface
public interface ISpellEffect {
	/**
	 * Called when this spell effect is applied by a caster.
     * The potency array is never null and is the same length as the number of Element values,
     * this array contains the potency of an element corresponding to the Element ordinal
     * channel tick is the amount of ticks this effect has been applied before
	 * @param caster the player casting a spell with this effect
	 * @param potencies the potency array
	 * @param channelTick the number of ticks this effect has been channeled
     * @return false to stop channeling this spell
     */
	boolean apply(EntityPlayer caster, int[] potencies, int channelTick);
}
