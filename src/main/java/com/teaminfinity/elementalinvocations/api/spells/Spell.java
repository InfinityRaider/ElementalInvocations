/*
 */
package com.teaminfinity.elementalinvocations.api.spells;

import com.teaminfinity.elementalinvocations.api.Element;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;

/**
 *
 * @author RlonRyan
 */
public class Spell {

	private final String spellId;
	private final int coolTicks;
	private final boolean isChanneled;
	private final List<Element> spellElements;
	private final List<ISpellRequirement> spellRequirements;
	private final List<ISpellEffect> spellEffects;

	public Spell(String spellId, int coolTicks, boolean isChanneled, List<Element> spellElements, List<ISpellRequirement> spellRequirements, List<ISpellEffect> spellEffects) {
		this.spellId = spellId;
		this.coolTicks = coolTicks;
		this.isChanneled = isChanneled;
		this.spellElements = spellElements;
		this.spellRequirements = spellRequirements;
		this.spellEffects = spellEffects;
	}

	public String getSpellId() {
		return spellId;
	}

	public int getCoolTicks() {
		return coolTicks;
	}

	public List<Element> getSpellElements() {
		return spellElements;
	}

	public List<ISpellRequirement> getSpellRequirements() {
		return spellRequirements;
	}

	public List<ISpellEffect> getSpellEffects() {
		return spellEffects;
	}

	public boolean invoke(EntityPlayer player) {
		if (this.spellRequirements.stream().allMatch(r -> r.isMet(player))) {
			this.spellEffects.forEach(e -> e.apply(player));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Spell && ((Spell)obj).spellId.equals(this.spellId);
	}

}
