/*
 */
package com.teaminfinity.elementalinvocations.impl.spell;

import com.teaminfinity.elementalinvocations.api.spells.ISpellRegistry;
import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpell;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author RlonRyan
 */
public final class SpellRegistry implements ISpellRegistry {
	
	private static final SpellRegistry INSTANCE = new SpellRegistry();
	
	private final Map<String, ISpell> spells;

	public SpellRegistry() {
		this.spells = new ConcurrentHashMap<>();
	}
	
	public static SpellRegistry getInstance() {
		return INSTANCE;
	}
	
	@Override
	public Optional<ISpell> addSpell(ISpell spell) {
		return Optional.ofNullable(spells.put(spell.getId(), spell));
	}
	
	@Override
	public Optional<ISpell> getSpell(String spellId) {
		return Optional.ofNullable(spells.get(spellId));
	}
	
	@Override
	public Optional<ISpell> getSpell(Element... elements) {
		return getSpell(Arrays.asList(elements));
	}
	
	@Override
	public Optional<ISpell> getSpell(List<Element> elements) {
		return spells.values().stream()
				.filter(s -> s.getElements().containsAll(elements))
				.findAny();
	}

	@Override
	public List<String> getSpellIds() {
		return new ArrayList<>(spells.keySet());
	}
	
	@Override
	public List<ISpell> getSpells() {
		return new ArrayList<>(spells.values());
	}
	
}
