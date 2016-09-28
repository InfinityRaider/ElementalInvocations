# Elemental Invocations [![build]][build-link]
The Modding Trials 2016 mod

## Contact [![discord]][discord-link]
I have a Slack channel where you can contact me for support/suggestions which you don't think fit on this issue tracker. Also if you want to discuss something with me, this is the place to be!


## Bug Reports [![bug]][bug-link]

Please report any and all bugs you might encounter while playing with this mod (this only applies to versions of Minecraft this mod is currently being developed for). Suggestions are also welcome.
However before reporting a bug please update to the latest version of the mod to see if it still persists.
If you want to post bug reports for older versions, make sure to tell me what version you are using and the version of Forge you are using.
If you report a bug and I request more feedback, the label 'Awaiting reply' will be added, if I have had no response for 5 days after adding that label, the issue will be closed.


## Mod description
Elemental Invocations is an elemental magic mod, there are six elements:
 - FIRE [F]:		focuses on dealing raw, area of effect damage
 - WATER [W]:	focuses on adapting itself
 - AIR [A]:		focuses on confusing and incapacitating enemies
 - EARTH [E]: 	focuses on defense
 - DEATH [D]:	focuses on soul reaping
 - LIFE [L]:		focuses on giving and creating life
	
The player chooses one affiliation and rises in levels of adeptness in magic, specifically his element.
Spellcasting happens by having a wand in the main/offhand. Wands have a certain core, attuned to one element.
Using a wand (right / left click) for right/left hand adds a charge of the wand's element to the player.
Using middle mouse button the player can invoke a spell based on the currently active charges.

Certain combinations of charges can be a spell shape. If the charges match a spell shape, that spell is cast.
If no shape is matched, an orb of magic is hurled instead. The effect and strength of the orb depends on the invoked charges.

Charges can be invoked unlimited, however there is a catch. Every time the player invokes a charge, an amount of instability is added.
The amount depends on the number of active charges and the nature of the charge. For example, a fire mage invoking a water charge will 
cause a lot more instability than a fire mage invoking a water charge. The further the element is from the caster's element on the magic circle, the higher the instability
The amount of instability a mage can handle depends on its magic level. Whenever a mage invokes a charge, there is a chance for the
combination of currently invoked charges to fizzle, the higher the instability, the higher this chance, the higher the mage's level, the lower this chance.
When magic fizzles, the effect of the magic orb which would be cast is applied to the player instead, be it with reduced effects.

###ELEMTAL CHARGE EFFECTS
	- FIRE:		fire damage, [secondary: chance to critical hit (double damage)]
	- WATER:	cold damage [secondary: chance to slows the target]
	- AIR:		lightning damag [secondary: chance to confuse the target]
	- EARTH:	physical damage [secondary: chance to knockback]
	- DEATH:	dark damage [secondary: reaps soul of the target if killed by this charge]
	- LIFE:		heals the target [secondary: heals the caster]


###LIST OF SPECIAL SPELL COMBINATIONS

FIRE SPELLS	

	- FIREBEAM (FIRE) [ F - F - F - F - F ]: 
		a laser of sheer heat which is channeled for a duration.
		This beam is also able to evaporate water
		
	- LIQUID FIRE (FIRE + WATER) [ F - F - F - W - W ]
		Invokes a wave of highly flammable liquid, entities hit by this wave are doused in the liquid and gain the flammable status condition.
		While flammable, if an entity is hit by fire damage, the entity is set on fire.
		
	- SUNSTRIKE (FIRE + AIR) [ F - F - F - A - A ]
		Invokes a solar flare which strikes a small area near the target. There is a small delay before the sunstrike is cast and when it arrives
		The sunstrike deals huge damage, but is hard to land. Only works under open sky. Blocks hit by the sunstrike will be set on fire
				
	- CONJURE METEOR (FIRE + EARTH)  [ F - F - F - E - E ]
		The player channels for a duration of time while a meteor (an entity) is spawned at very high y-level, behind the player.
		The meteor tracks towards the point the  player is aiming, the damage and steerability of the meteor depend on the charge levels.
	
	- DEATH NOVA (FIRE + DEATH) [ F - F - F - D - D ]
		If the target is below a certain % of hp, or a certain absuloute number (minimum of the two is taken), this spell explodes the entity.
		This explosion kills the target and damages entities around it. Entities killed by this will not drop loot.
	
	- PURIFYING FLAME (FIRE + LIFE) [F - F - F - L - L ]
		Damages target entity for a huge amount of damage, after which the amount of damage is healed back over time.


WATER SPELLS	

	- WAVEFORM (WATER) [ W - W - W -W - W ]
		The player conjures a wave off water at its feet. The wave travels with the player in the direction the player is looking and damages everything it passes.	
		
	- MIST FORM (WATER + FIRE) [ W - W - W - F - F ]
		While channeling, the player becomes a haze of mist and is invulnerable to all damage. Projectiles pass through and nothing can the player.
		The player can move freely, but can not perform any other actions

	- BALL LIGHTNING (WATER + AIR) [ W - W - W - A - A ]
		The player becomes a ball of lightning and quickly zaps towards the target location, damaging enemies which are passed	
		
	- FROST Armor (WATER + EARTH) [ W - W - W - E - E ]
		The caster becomes icy cold and everyone hitting the caster is slowed.
		Freezes the ground under the casters feet (apply layer of ice on normal blocks, and freeze water to ice, lava to obsidian + ice layer).
		Ice is very slippery.			
	
	- VENOMOUS AURA (WATER + DEATH) [ W - W - W - D - D ]
		The player generates a venomous aura, which withers plants and poisons enemies which are close to the player.
		
	- PHOTOFORM(WATER + LIFE) [ W - W - W - L - L ]
		Converts the player to a more vegetative form, the brighter it is, the faster the player moves and restores hunger.	


AIR SPELLS

	- CHAIN LIGHTNING (AIR) [ A - A - A - A - A ]
		Channeled spell which invokes lightning from the player's fingertips and jumps between targets	
		
	- CONJURE LIGHTNING (AIR + FIRE) [ A - A - A - F - F ]
		Instantly invokes a lightning strike at the target area which blinds and damages nearby entities
	
	- FREEZING FIELD (AIR + WATER) [ A - A - A - W - W ]
		Freezes an area to almost absolute zero, entities take damage over time while in the frozen area and are slowed,
		the longer they stay in the frozen zone, the more they are slowed down until they are frozen to the ground and can't move for a while	
	
	- STATIC FIELD (AIR + EARTH) [ A - A - A - E - E ]
		Conjures a field of static electricity which zaps entities inside for mild damage. Entities inside the static field gain twice
		the instability when invoking charges
		
	- VACUUM (AIR + DEATH) [ A - A - A - D - D ]
		Removes the air in an area, sucking all nearby entities to the center of that area
	
	- STATIC REMNANT (AIR + LIFE) [ A - A - A - L - L ]
		Leaves an image of static charge behind on the player's location. Entities coming close to this remnant get shocked for decent damage.


EARTH SPELLS	

	- MAGNETIZE (EARTH) [ E - E - E - E - E ]
		A number of stones is lifted into the air and orbits the player. The stones hurt entities which are hit and are destroyed after a number of hits.
		The stones also serve as a shield against incoming projectiles. While casting this spell, the player must focus and can not perform other actions.
		If the player does something else, focus is lost and all stones drop to the ground again. However the player can choose to hurl stones one by one
		by using the weapon attuned to EARTH.
		
	- GEOTHERMIC SURGE (EARTH + FIRE) [ E - E - E - F - F ]
		Draws heat from the core of the planet to the surface to make a scalding hot area. water starts boiling, hurting entities in the water
		and the ground becomes half molten, setting entities on fire.
	
	- WATER SPOUT (EARTH + WATER) [ E - E - E - W - W ]
		splits the earth, conjuring a spout of water from underground at the target location.
		All entities are hurled into the air and damaged by the water spout.
		
	- SOLID AIR (EARTH + AIR) [ E - E - E - A - A ]
		Conjures a temporary wall, which is invisible to enemies, but transparent for the caster.
		The caster can walk through this wall, but projectiles and enemies can not.			
	
	- AURA OF MEDUSA (EARTH + DEATH) [ E - E - E - D - D ]
		You move 50% slower, but enemies looking at you are slowly converted to stone and petrified, slowing their movements and turn rates.
		When an enemy is fully petrified, it can no longer move or perform actions. Petrified targets take reduced damage.
	
	- LIVING ARMOR (EARTH + LIFE) [ E - E - E - L - L ]
		Cloaks the player in living armor, healing the player over time and preventing incoming damage up to a threshold. The effect ends prematurely if the 
		damage threshold is reached


DEATH SPELLS

	- WRAITH FORM (DEATH) [ D - D - D - D - D ]
		Kills the caster and the caster becomes a wraith, invisible to other players and entities. While in wraith form, 
		the player has to consume souls over time to stay alive. The wraith can also not interact with the world (chests, beds, entities...).
		The exception is that the wraith can possess and take over the body of a lesser being (zombies or villagers) and regenerate his old body in the newly aquired mortal vessel. If no new body is found before the wraith runs out of souls to consume, the wraith, and thus the player dies.
	
	- REQUIEM OF SOULS (DEATH + FIRE) [ D - D - D - F - F ]
		Releases half of the harvested souls to unleash blasts of energy around the caster, amount of blasts depends on number of souls.
		Blasts do tremendous damage. 
				
	- MALIFICE(DEATH + WATER) [ D - D - D - W - W ]
		Releases one soul which homes in on the target. When the ghastly soul impacts the target, it removes 2 bars of hunger and causes hunger condition
		
	- REPLICATE (DEATH + AIR) [ D - D - D - A - A ]
		Consumes souls to summon replicas. The player can freely switch between each replica. All replicas do the same actions as the player:
		If the player turns, the replicas turn, if the player moves, the replicas move. If the player casts a spell, the replicas cast a spell.
		However replicas serve only as distractions and can not do damage and their spells will not have effect.
		
	- NECROMANCY (DEATH + EARTH) [ D - D - D - E - E ]
		Consumes harvested souls to summon a number of zombies and skeletons to fight for you
	
	- CONSUME SOUL (DEATH + LIFE) [ D - D - D - L - L ]
		Consumes one harvested soul to restore health


LIFE SPELLS

	- CLEANSING BURST (LIFE) [ L  - L - L - L - L ]
		Removes debuffs from target entity (or yourself)
		
	- FORGE ELEMENTALS(LIFE + FIRE) [ L - L - L - F - F ]
		Conjures entities which spew hot liquid magma at enemies
		
	- UP ROOTS AND RUN(LIFE + WATER) [ L - L - L - W - W ]
		Chanelled beam cast on trees, when fully channeled, converts a tree into an ent: slow, high hp, but heavy hitting entity which fights for you
	
	- HEALING WIND (LIFE + AIR) [ L - L - L - A - A ]
		Conjures a wind which applies regeneration to nearby allies
	
	- CONJURE GOLEM (LIFE + EARTH) [ L - L - L - E - E ]
		Chanelled beam which grabs dirt and stone to construct a golem to fight for you
	
	- CONVERSION(LIFE + DEATH) [ L - L - L - D - D ]
		Channelled beam which converts a target creature to fight for you instead, channeling duration depends on the entity



[build-link]:https://travis-ci.org/InfinityRaider/ElementalInvocations
[build]:https://travis-ci.org/InfinityRaider/ElementalInvocations.svg?branch=master "Travis-CI Build Status"
[discord-link]:https://discord.gg/9qfhaWc "InfinityRaider Discord"
[discord]:https://img.shields.io/badge/discord-InfinityRaider-ff69b4.svg "InfinityRaider Slack"
[bug-link]:https://www.github.com/InfinityRaider/ElementalInvocations/issues
[bug]:https://img.shields.io/badge/issue-bug-aa1111.svg
