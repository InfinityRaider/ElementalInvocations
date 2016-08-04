/*
 */
package com.teaminfinity.elementalinvocations;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.magic.effects.DamageEffect;
import com.teaminfinity.elementalinvocations.magic.effects.FireEffect;
import com.teaminfinity.elementalinvocations.magic.spell.SpellBuilder;
import com.teaminfinity.elementalinvocations.magic.spell.SpellRegistry;

/**
 *
 * @author RlonRyan
 */
public class SpellInitializer {

	public static final void init() {
		SpellRegistry.getInstance().addSpell(new SpellBuilder()
				.setSpellDescription("A fiery nova!", "Does something smell burnt to you?")
				.setSpellElements(Element.FIRE, Element.FIRE, Element.FIRE)
				.setSpellEffects(new FireEffect())
				.createSpell("Fire Nova", 0, false)
		);
		
		SpellRegistry.getInstance().addSpell(new SpellBuilder()
				.setSpellDescription("Damage all the entities!", "Ouch! That hurt!")
				.setSpellElements(Element.WATER, Element.WATER, Element.WATER)
				.setSpellEffects(new DamageEffect())
				.createSpell("Vortex", 0, false)
		);
	}

}
