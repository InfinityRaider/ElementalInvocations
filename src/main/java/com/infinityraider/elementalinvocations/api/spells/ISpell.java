/*
 */
package com.infinityraider.elementalinvocations.api.spells;

import com.google.common.collect.ImmutableList;
import com.infinityraider.elementalinvocations.api.Element;
import net.minecraft.entity.player.EntityPlayer;

public interface ISpell {

	String getId();

	int getCoolTicks();

	ImmutableList<String> getDescription();

	ImmutableList<Element> getElements();
	
	ImmutableList<ISpellRequirement> getRequirements();
	
	ImmutableList<ISpellEffect> getEffects();

	boolean invoke(EntityPlayer caster, int[] power);
	
	default boolean equals(ISpell spell) {
		return this.getId().equals(spell.getId()) || this.getElements().equals(spell.getElements());
	}
	
}
