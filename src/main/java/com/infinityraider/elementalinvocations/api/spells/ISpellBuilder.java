/*
 */
package com.infinityraider.elementalinvocations.api.spells;

import com.infinityraider.elementalinvocations.magic.spell.SpellBuilder;
import com.infinityraider.elementalinvocations.api.Element;
import java.util.List;

public interface ISpellBuilder {

	SpellBuilder setSpellDescription(String... lines);

	SpellBuilder setSpellDescription(List<String> lines);

	SpellBuilder setSpellElements(Element... elements);

	SpellBuilder setSpellElements(List<Element> elements);

	SpellBuilder setSpellRequirements(ISpellRequirement... requirements);

	SpellBuilder setSpellRequirements(List<ISpellRequirement> requirements);
	
	SpellBuilder setSpellEffects(ISpellEffect... effects);

	SpellBuilder setSpellEffects(List<ISpellEffect> effects);
	
	ISpell createSpell(String spellId, int coolTicks);
	
}
