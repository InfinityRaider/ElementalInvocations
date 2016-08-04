/*
 */
package com.teaminfinity.elementalinvocations.impl.spell;

import com.teaminfinity.elementalinvocations.api.spells.ISpell;
import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.api.spells.ISpellRequirement;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;

/**
 *
 * @author RlonRyan
 */
public class Spell implements ISpell {

	private final String id;
	private final int coolTicks;
	private final boolean channeled;
	private final List<String> description;
	private final List<Element> elements;
	private final List<ISpellRequirement> requirements;
	private final List<ISpellEffect> effects;

	public Spell(String id, int coolTicks, boolean channeled, List<String> description, List<Element> elements, List<ISpellRequirement> requirements, List<ISpellEffect> effects) {
		this.id = id;
		this.coolTicks = coolTicks;
		this.channeled = channeled;
		this.description = description;
		this.elements = elements;
		this.requirements = requirements;
		this.effects = effects;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public int getCoolTicks() {
		return coolTicks;
	}

	@Override
	public boolean isChanneled() {
		return channeled;
	}

	@Override
	public List<String> getDescription() {
		return description;
	}

	@Override
	public List<Element> getElements() {
		return elements;
	}

	@Override
	public List<ISpellRequirement> getRequirements() {
		return requirements;
	}

	@Override
	public List<ISpellEffect> getEffects() {
		return effects;
	}

	@Override
	public boolean invoke(EntityPlayer player) {
		if (this.requirements.stream().allMatch(r -> r.isMet(player))) {
			this.effects.forEach(e -> e.apply(player));
			return true;
		} else {
			return false;
		}
	}

}
