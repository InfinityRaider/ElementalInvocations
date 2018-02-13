/*
 */
package com.infinityraider.elementalinvocations.magic.spell;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.magic.spell.air.*;
import com.infinityraider.elementalinvocations.magic.spell.death.EffectVacuum;
import com.infinityraider.elementalinvocations.magic.spell.death.EffectNecromancy;
import com.infinityraider.elementalinvocations.magic.spell.death.EffectReplicate;
import com.infinityraider.elementalinvocations.magic.spell.earth.EffectLivingArmor;
import com.infinityraider.elementalinvocations.magic.spell.earth.EffectSolidAir;
import com.infinityraider.elementalinvocations.magic.spell.fire.*;
import com.infinityraider.elementalinvocations.magic.spell.life.EffectSummonEnt;
import com.infinityraider.elementalinvocations.magic.spell.earth.EffectFrostArmor;
import com.infinityraider.elementalinvocations.magic.spell.water.EffectMistForm;
import com.infinityraider.elementalinvocations.magic.spell.water.EffectFrozenSoil;
import com.infinityraider.elementalinvocations.magic.spell.water.EffectWaveForm;
import com.infinityraider.elementalinvocations.magic.spell.earth.EffectMagnetize;

public class SpellInitializer {

	public static void init() {
        //Life spells
        //-----------
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Up Roots and Run", "I ... aaaaam ... noooo ... treeeeee")
                .setSpellElements(Element.LIFE, Element.LIFE, Element.LIFE, Element.LIFE, Element.LIFE)
                .setSpellEffects(new EffectSummonEnt())
                .createSpell("summon_ent", 0));

        /*
        //TODO: need idea
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("", "")
                .setSpellElements(Element.LIFE, Element.LIFE, Element.LIFE, Element.AIR, Element.AIR)
                .setSpellEffects()
                .createSpell("", 0));
        */

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Rebirth!", "From the ashes I shall return")
                .setSpellElements(Element.LIFE, Element.LIFE, Element.LIFE, Element.FIRE, Element.FIRE)
                .setSpellEffects()
                .createSpell("solar_rebirth", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Life Drain", "Delicious, nutritious life force")
                .setSpellElements(Element.LIFE, Element.LIFE, Element.LIFE, Element.DEATH, Element.DEATH)
                .setSpellEffects()
                .createSpell("drain_life", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Conjure Golem", "The earth comes alive")
                .setSpellElements(Element.LIFE, Element.LIFE, Element.LIFE, Element.EARTH, Element.EARTH)
                .setSpellEffects()
                .createSpell("conjure_golem", 0));

        /*
        //TODO: need idea
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Rebirth!", "From the ashes I shall return")
                .setSpellElements(Element.LIFE, Element.LIFE, Element.LIFE, Element.WATER, Element.WATER)
                .setSpellEffects()
                .createSpell("solar_rebirth", 0));
        */


        //Air spells
        //----------
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Static Remnant", "Who's that handsome devil?")
                .setSpellElements(Element.AIR, Element.AIR, Element.AIR, Element.LIFE, Element.LIFE)
                .setSpellEffects(new EffectStaticRemnant())
                .createSpell("static_remnant", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Chain Lightning", "POWER, UNLIMITED POWER!")
                .setSpellElements(Element.AIR, Element.AIR, Element.AIR, Element.AIR, Element.AIR)
                .setSpellEffects(new EffectChainLightning())
                .createSpell("chain_lightning", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Lightning Storm", "Pikachu would be proud")
                .setSpellElements(Element.AIR, Element.AIR, Element.AIR, Element.FIRE, Element.FIRE)
                .setSpellEffects(new EffectLightningStorm())
                .createSpell("lightning_storm", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Tornado", "My foes aloft!")
                .setSpellElements(Element.AIR, Element.AIR, Element.AIR, Element.DEATH, Element.DEATH)
                .setSpellEffects(new EffectTornado())
                .createSpell("tornado", 0));

        /*
        //TODO: need idea
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("", "")
                .setSpellElements(Element.AIR, Element.AIR, Element.AIR, Element.EARTH, Element.EARTH)
                .setSpellEffects()
                .createSpell("", 0));
        */

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Ball Lighning", "Ziiiiiiiiiip!")
                .setSpellElements(Element.AIR, Element.AIR, Element.AIR, Element.WATER, Element.WATER)
                .setSpellEffects(new EffectBallLightning())
                .createSpell("wave_form", 0));


        //Fire spells
        //-----------
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Fire Spirit", "Guardian of flame")
                .setSpellElements(Element.FIRE, Element.FIRE, Element.FIRE, Element.LIFE, Element.LIFE)
                .setSpellEffects(new EffectSunstrike())
                .createSpell("fire_spirit", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Sunstrike", "The skies set you aflame")
                .setSpellElements(Element.FIRE, Element.FIRE, Element.FIRE, Element.AIR, Element.AIR)
                .setSpellEffects(new EffectSunstrike())
                .createSpell("sunstrike", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Firebeam", "Feel the heat")
                .setSpellElements(Element.FIRE, Element.FIRE, Element.FIRE, Element.FIRE, Element.FIRE)
                .setSpellEffects(new EffectFireBeam())
                .createSpell("firebeam", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Death Nova", "Meatsplosion!")
                .setSpellElements(Element.FIRE, Element.FIRE, Element.FIRE, Element.DEATH, Element.DEATH)
                .setSpellEffects(new EffectDeathNova())
                .createSpell("death_nova", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Conjure Meteor", "Behold the meatball!")
                .setSpellElements(Element.FIRE, Element.FIRE, Element.FIRE, Element.EARTH, Element.EARTH)
                .setSpellEffects(new EffectConjureMeteor())
                .createSpell("conjure_meteor", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Conjure Geyser", "The soil gets hot under your feet")
                .setSpellElements(Element.FIRE, Element.FIRE, Element.FIRE, Element.WATER, Element.WATER)
                .setSpellEffects(new EffectConjureGeyser())
                .createSpell("conjure_geyser", 0));


        //Death spells
        //------------
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Replicate", "Fool you once, fool you twice!")
                .setSpellElements(Element.DEATH, Element.DEATH, Element.DEATH, Element.LIFE, Element.LIFE)
                .setSpellEffects(new EffectReplicate())
                .createSpell("replicate", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Vacuum", "Well this sucks!")
                .setSpellElements(Element.DEATH, Element.DEATH, Element.DEATH, Element.AIR, Element.AIR)
                .setSpellEffects(new EffectVacuum())
                .createSpell("vacuum", 0));

        /*
        //TODO: need idea
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("", "")
                .setSpellElements(Element.DEATH, Element.DEATH, Element.DEATH, Element.FIRE, Element.FIRE)
                .setSpellEffects()
                .createSpell("", 0));
        */

        /*
        //TODO: need idea
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("")
                .setSpellElements(Element.DEATH, Element.DEATH, Element.DEATH, Element.DEATH, Element.DEATH)
                .setSpellEffects()
                .createSpell("", 0));
        */

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Necromancy", "Rise my minions!")
                .setSpellElements(Element.DEATH, Element.DEATH, Element.DEATH, Element.EARTH, Element.EARTH)
                .setSpellEffects(new EffectNecromancy())
                .createSpell("necromancy", 0));

        /*
        //TODO: need idea
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("")
                .setSpellElements(Element.DEATH, Element.DEATH, Element.DEATH, Element.WATER, Element.WATER)
                .setSpellEffects()
                .createSpell("", 0));
        */


        //Earth spells
        //------------
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Living Armor", "I'm in my safe spot now")
                .setSpellElements(Element.EARTH, Element.EARTH, Element.EARTH, Element.LIFE, Element.LIFE)
                .setSpellEffects(new EffectLivingArmor())
                .createSpell("living_armor", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Solid Air", "Mr. Mime could learn a thing or two from this")
                .setSpellElements(Element.EARTH, Element.EARTH, Element.EARTH, Element.AIR, Element.AIR)
                .setSpellEffects(new EffectSolidAir())
                .createSpell("solid_air", 0));

        /*
        //TODO: need idea
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("", "")
                .setSpellElements(Element.EARTH, Element.EARTH, Element.EARTH, Element.FIRE, Element.FIRE)
                .setSpellEffects()
                .createSpell("", 0));
        */

		SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Magnetize", "The earth moves me, and I move the earth")
                .setSpellElements(Element.EARTH, Element.EARTH, Element.EARTH, Element.EARTH, Element.EARTH)
                .setSpellEffects(new EffectMagnetize())
                .createSpell("magnetize", 0));

        /*
        //TODO: need idea
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("", "")
                .setSpellElements(Element.EARTH, Element.EARTH, Element.EARTH, Element.DEATH, Element.DEATH)
                .setSpellEffects()
                .createSpell("", 0));
        */

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Frost Armor", "Stay frosty!")
                .setSpellElements( Element.EARTH, Element.EARTH, Element.EARTH, Element.WATER, Element.WATER)
                .setSpellEffects(new EffectFrostArmor())
                .createSpell("frost_armor", 0));


        //Water spells
        //------------
        /*
        //TODO: need idea
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("", "")
                .setSpellElements(Element.WATER, Element.WATER, Element.WATER, Element.LIFE, Element.LIFE)
                .setSpellEffects()
                .createSpell("", 0));
        */

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Mist Form", "Can't touch this")
                .setSpellElements(Element.WATER, Element.WATER, Element.WATER, Element.AIR, Element.AIR)
                .setSpellEffects(new EffectMistForm())
                .createSpell("mist_form", 0));

        /*
        //TODO: need idea
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("", "")
                .setSpellElements(Element.WATER, Element.WATER, Element.WATER, Element.FIRE, Element.FIRE)
                .setSpellEffects())
                .createSpell("", 0));
        */

        /*
        //TODO: need idea
        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("", "")
                .setSpellElements(Element.WATER, Element.WATER, Element.WATER, Element.DEATH, Element.DEATH)
                .setSpellEffects())
                .createSpell("", 0));
        */

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Frozen Soil", "Got cold feet yet?")
                .setSpellElements(Element.WATER, Element.WATER, Element.WATER, Element.EARTH, Element.EARTH)
                .setSpellEffects(new EffectFrozenSoil())
                .createSpell("frozen_soil", 0));

        SpellRegistry.getInstance().addSpell(new SpellBuilder()
                .setSpellDescription("Wave Form", "Surf's Up!")
                .setSpellElements(Element.WATER, Element.WATER, Element.WATER, Element.WATER, Element.WATER)
                .setSpellEffects(new EffectWaveForm())
                .createSpell("wave_form", 0));


	}

}
