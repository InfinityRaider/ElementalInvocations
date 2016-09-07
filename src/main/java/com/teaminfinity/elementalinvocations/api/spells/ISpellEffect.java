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

    /**
     * Some spell effects can linger for a while, even though the player  is not channeling.
     * Return true from this method to have this effect linger.
     * Lingering effects will have the lingerUpdate() method called every tick.
     * Lingering effects will have the spellContextAction() method called when the player presses the spell context key bind
     * @return true to make this effect lingering
     */
	default boolean isLingeringEffect() {
        return false;
    }

    /**
     * Called every tick for lingering effects
     * @param caster the player who cast this spell effect and has it as a lingering effect
     * @return true to end this effect
     */
    default boolean lingerUpdate(EntityPlayer caster) {
        return true;
    }

    /**
     * Called when the caster presses the spell context key bind,
     * @param caster the player who cast this spell effect and has it as a lingering effect
     * @return true to end this effect
     */
    default boolean spellContextAction(EntityPlayer caster) {
        return false;
    }
}
