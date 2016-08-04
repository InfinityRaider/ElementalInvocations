/*
 */
package com.teaminfinity.elementalinvocations.api.spells;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

/**
 *
 */
@FunctionalInterface
public interface ISpellEffect {
	
	void apply(EntityPlayer player, Vec3d target, int power);
	
}
