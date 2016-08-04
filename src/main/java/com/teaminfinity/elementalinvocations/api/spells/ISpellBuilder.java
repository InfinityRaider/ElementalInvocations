/*
 */
package com.teaminfinity.elementalinvocations.api.spells;

import com.teaminfinity.elementalinvocations.impl.spell.SpellBuilder;
import com.teaminfinity.elementalinvocations.api.Element;
import java.util.List;

/**
 *
 * @author RlonRyan
 */
public interface ISpellBuilder {

	SpellBuilder setSpellDescription(String... lines);

	SpellBuilder setSpellDescription(List<String> lines);

	SpellBuilder setSpellElements(Element... elements);

	SpellBuilder setSpellElements(List<Element> elements);

	SpellBuilder setSpellRequirements(ISpellRequirement... requirements);

	SpellBuilder setSpellRequirements(List<ISpellRequirement> requirements);
	
	SpellBuilder setSpellEffects(ISpellEffect... effects);

	SpellBuilder setSpellEffects(List<ISpellEffect> effects);
	
	ISpell createSpell(String spellId, int coolTicks, boolean isChanneled);
	
}
