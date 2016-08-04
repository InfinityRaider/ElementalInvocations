/*
 */
package com.teaminfinity.elementalinvocations.api.spells;

import com.teaminfinity.elementalinvocations.api.Element;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SpellBuilder {

	private final String spellId;
	private final int coolTicks;
	private final boolean isChanneled;
	private final List<Element> spellElements;
	private final List<ISpellRequirement> spellRequirements;
	private final List<ISpellEffect> spellEffects;

	public SpellBuilder(String spellId, int coolTicks, boolean isChanneled) {
		this.spellId = spellId;
		this.coolTicks = coolTicks;
		this.isChanneled = isChanneled;
		this.spellElements = new ArrayList<>();
		this.spellRequirements = new ArrayList<>();
		this.spellEffects = new ArrayList<>();
	}
	
	public SpellBuilder addSpellElements(List<Element> elements) {
		this.spellElements.addAll(elements);
		return this;
	}
	
	public SpellBuilder addSpellElements(Element... elements) {
		this.spellElements.addAll(Arrays.asList(elements));
		return this;
	}
	
	public SpellBuilder addSpellRequirements(List<ISpellRequirement> requirements) {
		this.spellRequirements.addAll(requirements);
		return this;
	}
	
	public SpellBuilder addSpellRequirements(ISpellRequirement... requirements) {
		this.spellRequirements.addAll(Arrays.asList(requirements));
		return this;
	}
	
	public SpellBuilder addSpellEffects(List<ISpellEffect> effects) {
		this.spellEffects.addAll(effects);
		return this;
	}
	
	public SpellBuilder addSpellEffects(ISpellEffect... effects) {
		this.spellEffects.addAll(Arrays.asList(effects));
		return this;
	}

	public Spell createSpell() {
		return new Spell(spellId, coolTicks, isChanneled, spellElements, spellRequirements, spellEffects);
	}
	
}
