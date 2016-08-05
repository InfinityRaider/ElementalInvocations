/*
 */
package com.teaminfinity.elementalinvocations.magic.spell.misc;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.utility.AreaHelper;
import com.teaminfinity.elementalinvocations.utility.LogHelper;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

public class FireEffect implements ISpellEffect {
	@Override
	public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
		LogHelper.debug("Applying Fire Effect!\n\tTarget: ({0})\n\tPower: {1}", caster.getPositionVector(), potencies);
		AxisAlignedBB area = AreaHelper.getArea(caster.getPositionVector(), potencies[Element.FIRE.ordinal()]);
		List<Entity> entities = caster.getEntityWorld().getEntitiesWithinAABB(Entity.class, area);
		entities.forEach(e -> e.setFire(1));
		return false;
	}
	
}
