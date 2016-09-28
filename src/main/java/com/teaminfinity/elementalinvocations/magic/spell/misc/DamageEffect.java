/*
 */
package com.teaminfinity.elementalinvocations.magic.spell.misc;

import com.infinityraider.infinitylib.utility.LogHelper;
import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.utility.AreaHelper;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;

public class DamageEffect implements ISpellEffect {
	@Override
	public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
		LogHelper.debug("Applying Damage Effect!\n\tTarget: ({0})\n\tPower: {1}", caster.getPositionVector(), potencies);
		AxisAlignedBB area = AreaHelper.getArea(caster.getPositionVector(), potencies[Element.DEATH.ordinal()]);
		List<Entity> ents = caster.getEntityWorld().getEntitiesWithinAABB(Entity.class, area);
		ents.forEach(e -> e.attackEntityFrom(DamageSource.magic, potencies[Element.DEATH.ordinal()]));
		return false;
	}
	
}
