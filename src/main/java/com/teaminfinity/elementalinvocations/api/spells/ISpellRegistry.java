/*
 */
package com.teaminfinity.elementalinvocations.api.spells;

import com.teaminfinity.elementalinvocations.api.Element;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author RlonRyan
 */
public interface ISpellRegistry {

	boolean addSpell(ISpell spell);

	Optional<ISpell> getSpell(String name);
	
	Optional<ISpell> getSpell(Element... elements);

	Optional<ISpell> getSpell(List<Element> elements);

	Set<ISpell> getSpells();
	
}
