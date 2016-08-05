/*
 */
package com.teaminfinity.elementalinvocations;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.magic.effects.*;
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
				.setSpellElements(Element.DEATH, Element.DEATH, Element.DEATH)
				.setSpellEffects(new DamageEffect())
				.createSpell("Hit", 0, false)
		);
		
		SpellRegistry.getInstance().addSpell(new SpellBuilder()
				.setSpellDescription("Drown all the entities!", "You awake now?")
				.setSpellElements(Element.WATER, Element.WATER, Element.WATER)
				.setSpellEffects(new ExtinguishEffect())
				.createSpell("Vortex", 0, false)
		);
	}

}
