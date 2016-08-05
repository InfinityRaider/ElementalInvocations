/*
 */
package com.teaminfinity.elementalinvocations.api.spells;

import com.google.common.collect.ImmutableList;
import com.teaminfinity.elementalinvocations.api.Element;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public interface ISpell {

	String getId();

	int getCoolTicks();
	
	boolean isChanneled();

	ImmutableList<String> getDescription();

	ImmutableList<Element> getElements();
	
	ImmutableList<ISpellRequirement> getRequirements();
	
	ImmutableList<ISpellEffect> getEffects();

	boolean invoke(EntityPlayer player, Vec3d target, int[] power);
	
	default boolean equals(ISpell spell) {
		return this.getId().equals(spell.getId()) || this.getElements().equals(spell.getElements());
	}
	
}
