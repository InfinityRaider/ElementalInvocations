/*
 */
package com.teaminfinity.elementalinvocations.magic.spell;

import com.google.common.collect.ImmutableList;
import com.teaminfinity.elementalinvocations.api.spells.ISpell;
import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.api.spells.ISpellRequirement;
import java.util.Collections;
import java.util.List;

import com.teaminfinity.elementalinvocations.handler.SpellCastingHandler;
import net.minecraft.entity.player.EntityPlayer;

public class Spell implements ISpell {

	private final String id;
	private final int coolTicks;
	private final ImmutableList<String> description;
	private final ImmutableList<Element> elements;
	private final ImmutableList<ISpellRequirement> requirements;
	private final ImmutableList<ISpellEffect> effects;

	public Spell(String id, int coolTicks, List<String> description, List<Element> elements, List<ISpellRequirement> requirements, List<ISpellEffect> effects) {
		Collections.sort(elements);
		this.id = id;
		this.coolTicks = coolTicks;
		this.description = ImmutableList.copyOf(description);
		this.elements = ImmutableList.copyOf(elements);
		this.requirements = ImmutableList.copyOf(requirements);
		this.effects = ImmutableList.copyOf(effects);
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
	public ImmutableList<String> getDescription() {
		return description;
	}

	@Override
	public ImmutableList<Element> getElements() {
		return elements;
	}

	@Override
	public ImmutableList<ISpellRequirement> getRequirements() {
		return requirements;
	}

	@Override
	public ImmutableList<ISpellEffect> getEffects() {
		return effects;
	}

	@Override
	public boolean invoke(EntityPlayer caster, int[] power) {
		if (this.requirements.stream().allMatch(r -> r.isMet(caster))) {
            SpellCastingHandler.getInstance().castSpell(caster, this, power);
			return true;
		} else {
			return false;
		}
	}

}
