/*
 */
package com.teaminfinity.elementalinvocations.api;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.api.spells.ISpellRequirement;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;

/**
 *
 * @author RlonRyan
 */
public interface ISpell {
	
	String getId();

	int getCoolTicks();
	
	boolean isChanneled();

	List<String> getDescription();

	List<Element> getElements();
	
	List<ISpellRequirement> getRequirements();
	
	List<ISpellEffect> getEffects();

	boolean invoke(EntityPlayer player);
	
	default boolean equals(ISpell spell) {
		return this.getId().equals(spell.getId());
	}
	
}
