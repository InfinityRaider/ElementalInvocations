/*
 */
package com.infinityraider.elementalinvocations.magic.spell;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.magic.spell.air.EffectVacuum;
import com.infinityraider.elementalinvocations.magic.spell.death.EffectConsumeSoul;
import com.infinityraider.elementalinvocations.magic.spell.death.EffectNecromancy;
import com.infinityraider.elementalinvocations.magic.spell.death.EffectReplicate;
import com.infinityraider.elementalinvocations.magic.spell.earth.EffectLivingArmor;
import com.infinityraider.elementalinvocations.magic.spell.fire.*;
import com.infinityraider.elementalinvocations.magic.spell.misc.DamageEffect;
import com.infinityraider.elementalinvocations.magic.spell.misc.ExtinguishEffect;
import com.infinityraider.elementalinvocations.magic.spell.misc.FireEffect;
import com.infinityraider.elementalinvocations.magic.spell.water.EffectBallLightning;
import com.infinityraider.elementalinvocations.magic.spell.water.EffectFrostArmor;
import com.infinityraider.elementalinvocations.magic.spell.water.EffectWaveForm;
import com.infinityraider.elementalinvocations.magic.spell.earth.EffectMagnetize;

public class SpellInitializer {

	public static void init() {
        //Fire spells
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Firebeam", "Feel the heat")
                .setSpellElements(Element.FIRE, Element.FIRE, Element.FIRE, Element.FIRE, Element.FIRE)
                .setSpellEffects(new EffectFireBeam())
                .createSpell("firebeam", 0));

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

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Death Nova", "Meatsplosion!")
                .setSpellElements(Element.FIRE, Element.FIRE, Element.FIRE, Element.DEATH, Element.DEATH)
                .setSpellEffects(new EffectDeathNova())
                .createSpell("death_nova", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Purifying Flame", "Cleansing fire!")
                .setSpellElements(Element.FIRE, Element.FIRE, Element.FIRE, Element.LIFE, Element.LIFE)
                .setSpellEffects(new EffectPurifyingFlame())
                .createSpell("purifying_flame", 0));


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

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Frost Armor", "Stay frosty!")
                .setSpellElements(Element.WATER, Element.WATER, Element.WATER, Element.EARTH, Element.EARTH)
                .setSpellEffects(new EffectFrostArmor())
                .createSpell("frost_armor", 0));


        //Air spells
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Vacuum", "Well this sucks!")
                .setSpellElements(Element.AIR, Element.AIR, Element.AIR, Element.DEATH, Element.DEATH)
                .setSpellEffects(new EffectVacuum())
                .createSpell("vacuum", 0));


        //Earth spells
		SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Magnetize", "The earth moves me, and I move the earth")
                .setSpellElements(Element.EARTH, Element.EARTH, Element.EARTH, Element.EARTH, Element.EARTH)
                .setSpellEffects(new EffectMagnetize())
                .createSpell("magnetize", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Living Armor", "I'm in my safe spot now")
                .setSpellElements(Element.EARTH, Element.EARTH, Element.EARTH, Element.LIFE, Element.LIFE)
                .setSpellEffects(new EffectLivingArmor())
                .createSpell("living_armor", 0));


        //Death spells
		SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Replicate", "Fool you once, fool you twice!")
                .setSpellElements(Element.DEATH, Element.DEATH, Element.DEATH, Element.AIR, Element.AIR)
                .setSpellEffects(new EffectReplicate())
                .createSpell("replicate", 0));

		SpellRegistry.getInstance().addSpell(new SpellBuilder()
				.setSpellDescription("Necromancy", "Rise my minions!")
				.setSpellElements(Element.DEATH, Element.DEATH, Element.DEATH, Element.EARTH, Element.EARTH)
				.setSpellEffects(new EffectNecromancy())
				.createSpell("necromancy", 0));

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
