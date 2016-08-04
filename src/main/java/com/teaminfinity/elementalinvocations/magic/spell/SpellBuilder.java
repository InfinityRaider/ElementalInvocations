/*
 */
package com.teaminfinity.elementalinvocations.magic.spell;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpellBuilder;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.api.spells.ISpellRequirement;
import java.util.Arrays;
import java.util.List;


public class SpellBuilder implements ISpellBuilder {

	private List<String> spellDescription;
	private List<Element> spellElements;
	private List<ISpellRequirement> spellRequirements;
	private List<ISpellEffect> spellEffects;
	
	@Override
	public SpellBuilder setSpellDescription(String... lines) {
		this.spellDescription = Arrays.asList(lines);
		return this;
	}
	
	@Override
	public SpellBuilder setSpellDescription(List<String> lines) {
		this.spellDescription = lines;
		return this;
	}
	
	@Override
	public SpellBuilder setSpellElements(Element... elements) {
		this.spellElements = Arrays.asList(elements);
		return this;
	}
	
	@Override
	public SpellBuilder setSpellElements(List<Element> elements) {
		this.spellElements = elements;
		return this;
	}
	
	@Override
	public SpellBuilder setSpellRequirements(ISpellRequirement... requirements) {
		this.spellRequirements = Arrays.asList(requirements);
		return this;
	}
	
	@Override
	public SpellBuilder setSpellRequirements(List<ISpellRequirement> requirements) {
		this.spellRequirements = requirements;
		return this;
	}
	
	@Override
	public SpellBuilder setSpellEffects(ISpellEffect... effects) {
		this.spellEffects = Arrays.asList(effects);
		return this;
	}
	
	@Override
	public SpellBuilder setSpellEffects(List<ISpellEffect> effects) {
		this.spellEffects = effects;
		return this;
	}

	@Override
	public Spell createSpell(String spellId, int coolTicks, boolean isChanneled) {
		return new Spell(spellId, coolTicks, isChanneled, spellDescription, spellElements, spellRequirements, spellEffects);
	}
	
}
