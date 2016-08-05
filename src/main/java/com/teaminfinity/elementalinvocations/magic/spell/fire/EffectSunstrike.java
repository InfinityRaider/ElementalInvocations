package com.teaminfinity.elementalinvocations.magic.spell.fire;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.entity.EntitySunstrike;
import com.teaminfinity.elementalinvocations.utility.TargetHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

public class EffectSunstrike implements ISpellEffect {
    @Override
    public void apply(EntityPlayer caster, int[] potencies) {
        RayTraceResult result = TargetHelper.getTarget(caster, 128);
        if(result == null) {
            return;
        }
        caster.worldObj.spawnEntityInWorld(new EntitySunstrike(
                        caster.worldObj, result.hitVec.xCoord, result.hitVec.zCoord,
                        potencies[Element.FIRE.ordinal()], potencies[Element.AIR.ordinal()]));
    }
}
