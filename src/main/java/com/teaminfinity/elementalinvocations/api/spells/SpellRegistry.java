/*
 */
package com.teaminfinity.elementalinvocations.api.spells;

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
public final class SpellRegistry {
	
	private static final Map<String, Spell> spells = new ConcurrentHashMap<>();

	private SpellRegistry() {
	}
	
	public static Spell addSpell(Spell spell) {
		return spell == null ? null : spells.put(spell.getSpellId(), spell);
	}
	
	public static Spell getSpell(String spellId) {
		return spells.get(spellId);
	}

	public static List<String> getSpellIds() {
		return new ArrayList<>(spells.keySet());
	}
	
	public static List<Spell> getSpells() {
		return new ArrayList<>(spells.values());
	}
	
	public static List<Spell> getSpells(Element... elements) {
		return getSpells(Arrays.asList(elements));
	}
	
	public static List<Spell> getSpells(List<Element> elements) {
		return spells.values().stream()
				.filter(s -> s.getSpellElements().containsAll(elements))
				.collect(Collectors.toList());
	}
	
}
