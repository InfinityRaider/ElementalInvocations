/*
 */
package com.infinityraider.elementalinvocations.magic.spell;

import com.infinityraider.elementalinvocations.entity.EntityMagicProjectile;
import net.minecraft.entity.player.EntityPlayer;

public class EffectDefault extends Spell {
	public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
		EntityMagicProjectile projectile = new EntityMagicProjectile(caster, potencies);
		caster.getEntityWorld().spawnEntityInWorld(projectile);
		return false;
	}

}
