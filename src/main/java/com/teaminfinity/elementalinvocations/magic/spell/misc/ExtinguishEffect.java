/*
 */
package com.teaminfinity.elementalinvocations.magic.spell.misc;

import com.teaminfinity.elementalinvocations.ElementalInvocations;
import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.utility.AreaHelper;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;

public class ExtinguishEffect implements ISpellEffect {
	@Override
	public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
		ElementalInvocations.instance.getLogger().debug("Applying Extinguish Effect!\n\tTarget: ({0})\n\tPower: {1}", caster.getPositionVector(), potencies);
		AxisAlignedBB area = AreaHelper.getArea(caster.getPositionVector(), potencies[Element.WATER.ordinal()]);
		List<Entity> entitiess = caster.getEntityWorld().getEntitiesWithinAABB(Entity.class, area);
		entitiess.forEach(Entity::extinguish);
		return false;
	}
	
}
