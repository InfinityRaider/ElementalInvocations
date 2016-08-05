package com.teaminfinity.elementalinvocations.magic.spell.water;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.entity.EntityBallLightning;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class EffectBallLightning implements ISpellEffect {
    @Override
    public void apply(EntityPlayer player, Vec3d target, int[] power) {
        EntityBallLightning ball = new EntityBallLightning(player, power[Element.AIR.ordinal()], power[Element.WATER.ordinal()]);
        player.getEntityWorld().spawnEntityInWorld(ball);
        player.startRiding(ball, true);
    }
}
