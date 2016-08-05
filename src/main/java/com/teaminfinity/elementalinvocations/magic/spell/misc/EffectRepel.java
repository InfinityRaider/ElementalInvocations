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
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

/**
 *
 */
public class EffectRepel implements ISpellEffect {

    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        final Vec3d target = caster.getPositionVector();
        final int power = potencies[Element.DEATH.ordinal()];
		final AxisAlignedBB area = AreaHelper.getArea(target, power);
		List<Entity> ents = caster.getEntityWorld().getEntitiesWithinAABB(Entity.class, area);
        for (Entity ent : ents) {
            Vec3d motion = target.subtract(ent.getPositionVector()).normalize().scale(power);
            ent.motionX = -motion.xCoord;
            ent.motionY = -motion.yCoord;
            ent.motionZ = -motion.zCoord;
        }
		return false;
    }
    
}
