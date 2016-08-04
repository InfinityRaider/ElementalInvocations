/*
 */
package com.teaminfinity.elementalinvocations.api.spells;

import com.teaminfinity.elementalinvocations.api.Element;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author RlonRyan
 */
public interface ISpellRegistry {

	Optional<ISpell> addSpell(ISpell spell);

	Optional<ISpell> getSpell(String spellId);
	
	Optional<ISpell> getSpell(Element... elements);

	Optional<ISpell> getSpell(List<Element> elements);

	List<String> getSpellIds();

	List<ISpell> getSpells();
	
}
