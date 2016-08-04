/*
 */
package com.teaminfinity.elementalinvocations.api.spells;

import com.teaminfinity.elementalinvocations.api.Element;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;

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
