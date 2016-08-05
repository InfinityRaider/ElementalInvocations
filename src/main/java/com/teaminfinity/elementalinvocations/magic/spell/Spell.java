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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

/**
 *
 */
public class Spell implements ISpell {

	private final String id;
	private final int coolTicks;
	private final boolean channeled;
	private final ImmutableList<String> description;
	private final ImmutableList<Element> elements;
	private final ImmutableList<ISpellRequirement> requirements;
	private final ImmutableList<ISpellEffect> effects;

	public Spell(String id, int coolTicks, boolean channeled, List<String> description, List<Element> elements, List<ISpellRequirement> requirements, List<ISpellEffect> effects) {
		Collections.sort(elements);
		this.id = id;
		this.coolTicks = coolTicks;
		this.channeled = channeled;
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
	public boolean isChanneled() {
		return channeled;
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
	public boolean invoke(EntityPlayer player, Vec3d target, int power) {
		if (this.requirements.stream().allMatch(r -> r.isMet(player))) {
			this.effects.forEach(e -> e.apply(player, target, power));
			return true;
		} else {
			return false;
		}
	}

}