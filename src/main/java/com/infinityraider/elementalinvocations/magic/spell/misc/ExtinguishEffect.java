/*
 */
package com.infinityraider.elementalinvocations.magic.spell.misc;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.utility.AreaHelper;
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
