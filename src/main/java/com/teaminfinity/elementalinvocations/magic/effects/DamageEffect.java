/*
 */
package com.teaminfinity.elementalinvocations.magic.effects;

import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.utility.AreaHelper;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

/**
 *
 */
public class DamageEffect implements ISpellEffect {

	@Override
	public void apply(EntityPlayer player, Vec3d target) {
		AxisAlignedBB area = AreaHelper.getArea(target, 5);
		List<Entity> ents = player.getEntityWorld().getEntitiesWithinAABB(Entity.class, area);
		ents.forEach(e -> e.attackEntityFrom(DamageSource.magic, 5));
	}
	
}
