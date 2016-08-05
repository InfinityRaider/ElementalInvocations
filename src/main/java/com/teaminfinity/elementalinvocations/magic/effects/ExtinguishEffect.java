/*
 */
package com.teaminfinity.elementalinvocations.magic.effects;

import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.utility.AreaHelper;
import com.teaminfinity.elementalinvocations.utility.LogHelper;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

/**
 *
 */
public class ExtinguishEffect implements ISpellEffect {

	@Override
	public void apply(EntityPlayer player, Vec3d target, int[] power) {
		LogHelper.debug("Applying Extinguish Effect!\n\tTarget: ({0})\n\tPower: {1}", target, power);
		AxisAlignedBB area = AreaHelper.getArea(target, power);
		List<Entity> ents = player.getEntityWorld().getEntitiesWithinAABB(Entity.class, area);
		ents.forEach(e -> e.extinguish());
	}
	
}
