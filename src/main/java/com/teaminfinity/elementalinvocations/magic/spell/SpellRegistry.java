/*
 */
package com.teaminfinity.elementalinvocations.magic.spell;

import com.teaminfinity.elementalinvocations.api.spells.ISpellRegistry;
import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpell;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class SpellRegistry implements ISpellRegistry {
	
	private static final SpellRegistry INSTANCE = new SpellRegistry();
	
	private final Set<ISpell> spells;

	public SpellRegistry() {
		this.spells = new HashSet<>();
	}
	
	public static SpellRegistry getInstance() {
		return INSTANCE;
	}
	
	@Override
	public boolean addSpell(ISpell spell) {
		return spells.add(spell);
	}
	
	@Override
	public Optional<ISpell> getSpell(String spellId) {
		return spells.stream()
				.filter(s -> s.getId().equals(spellId))
				.findAny();
	}
	
	@Override
	public Optional<ISpell> getSpell(Element... elements) {
		return getSpell(Arrays.asList(elements));
	}
	
	@Override
	public Optional<ISpell> getSpell(List<Element> elements) {
		Collections.sort(elements);
		return spells.stream()
				.filter(s -> s.getElements().equals(elements))
				.findAny();
	}
	
	@Override
	public Set<ISpell> getSpells() {
		return new HashSet<>(spells);
	}
	
}
