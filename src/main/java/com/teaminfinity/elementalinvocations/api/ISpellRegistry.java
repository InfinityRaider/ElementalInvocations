/*
 */
package com.teaminfinity.elementalinvocations.api;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.impl.spell.Spell;
import java.util.List;

/**
 *
 * @author RlonRyan
 */
public interface ISpellRegistry {

	Spell addSpell(Spell spell);

	Spell getSpell(String spellId);

	List<String> getSpellIds();

	List<Spell> getSpells();

	List<Spell> getSpells(Element... elements);

	List<Spell> getSpells(List<Element> elements);
	
}
