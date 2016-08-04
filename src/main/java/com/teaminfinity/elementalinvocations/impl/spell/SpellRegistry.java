/*
 */
package com.teaminfinity.elementalinvocations.impl.spell;

import com.teaminfinity.elementalinvocations.api.ISpellRegistry;
import com.teaminfinity.elementalinvocations.api.Element;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 *
 * @author RlonRyan
 */
public final class SpellRegistry implements ISpellRegistry {
	
	private static final SpellRegistry INSTANCE = new SpellRegistry();
	
	private final Map<String, Spell> spells;

	public SpellRegistry() {
		this.spells = new ConcurrentHashMap<>();
	}
	
	public static SpellRegistry getInstance() {
		return INSTANCE;
	}
	
	@Override
	public Spell addSpell(Spell spell) {
		return spell == null ? null : spells.put(spell.getId(), spell);
	}
	
	@Override
	public Spell getSpell(String spellId) {
		return spells.get(spellId);
	}

	@Override
	public List<String> getSpellIds() {
		return new ArrayList<>(spells.keySet());
	}
	
	@Override
	public List<Spell> getSpells() {
		return new ArrayList<>(spells.values());
	}
	
	@Override
	public List<Spell> getSpells(Element... elements) {
		return getSpells(Arrays.asList(elements));
	}
	
	@Override
	public List<Spell> getSpells(List<Element> elements) {
		return spells.values().stream()
				.filter(s -> s.getElements().containsAll(elements))
				.collect(Collectors.toList());
	}
	
}
