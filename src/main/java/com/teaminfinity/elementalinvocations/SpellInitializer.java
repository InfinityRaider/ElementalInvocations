/*
 */
package com.teaminfinity.elementalinvocations;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.magic.spell.SpellBuilder;
import com.teaminfinity.elementalinvocations.magic.spell.SpellRegistry;
import com.teaminfinity.elementalinvocations.magic.spell.death.EffectConsumeSoul;
import com.teaminfinity.elementalinvocations.magic.spell.fire.EffectConjureMeteor;
import com.teaminfinity.elementalinvocations.magic.spell.fire.EffectSunstrike;
import com.teaminfinity.elementalinvocations.magic.spell.misc.DamageEffect;
import com.teaminfinity.elementalinvocations.magic.spell.misc.ExtinguishEffect;
import com.teaminfinity.elementalinvocations.magic.spell.misc.FireEffect;
import com.teaminfinity.elementalinvocations.magic.spell.water.EffectBallLightning;
import com.teaminfinity.elementalinvocations.magic.spell.water.EffectWaveForm;

public class SpellInitializer {

	public static void init() {
        //Fire spells
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Sunstrike", "The skies set you aflame")
                .setSpellElements(Element.FIRE, Element.FIRE, Element.FIRE, Element.AIR, Element.AIR)
                .setSpellEffects(new EffectSunstrike())
                .createSpell("sunstrike", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Conjure Meteor", "Behold the meatball!")
                .setSpellElements(Element.FIRE, Element.FIRE, Element.FIRE, Element.EARTH, Element.EARTH)
                .setSpellEffects(new EffectConjureMeteor())
                .createSpell("conjure_meteor", 0));

        //Water spells
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Wave Form", "Surf's Up!")
                .setSpellElements(Element.WATER, Element.WATER, Element.WATER, Element.WATER, Element.WATER)
                .setSpellEffects(new EffectWaveForm())
                .createSpell("ball_lightning", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Ball Lighning", "Ziiiiiiiiiip!")
                .setSpellElements(Element.WATER, Element.WATER, Element.WATER, Element.AIR, Element.AIR)
                .setSpellEffects(new EffectBallLightning())
                .createSpell("wave_form", 0));

        //Air spells


        //Earth spells


        //Death spells
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Consume Soul", "You weren't using it were you?")
                .setSpellElements(Element.DEATH, Element.DEATH, Element.DEATH, Element.LIFE, Element.LIFE)
                .setSpellEffects(new EffectConsumeSoul())
                .createSpell("consume_soul", 0));



        //Life spells


        //Misc spells
		SpellRegistry.getInstance().addSpell(new SpellBuilder()
				.setSpellDescription("A fiery nova!", "Does something smell burnt to you?")
				.setSpellElements(Element.FIRE, Element.FIRE, Element.FIRE)
				.setSpellEffects(new FireEffect())
				.createSpell("Fire Nova", 0)
		);
		
		SpellRegistry.getInstance().addSpell(new SpellBuilder()
				.setSpellDescription("Damage all the entities!", "Ouch! That hurt!")
				.setSpellElements(Element.DEATH, Element.DEATH, Element.DEATH)
				.setSpellEffects(new DamageEffect())
				.createSpell("Hit", 0)
		);
		
		SpellRegistry.getInstance().addSpell(new SpellBuilder()
				.setSpellDescription("Drown all the entities!", "You awake now?")
				.setSpellElements(Element.WATER, Element.WATER, Element.WATER)
				.setSpellEffects(new ExtinguishEffect())
				.createSpell("Vortex", 0)
		);

	}

}
