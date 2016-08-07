package com.teaminfinity.elementalinvocations.magic.spell.air;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.entity.EntityVacuum;
import com.teaminfinity.elementalinvocations.utility.TargetHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

public class EffectVacuum implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        RayTraceResult target = TargetHelper.getTargetBlock(caster, 64);
        if(target != null && target.hitVec != null) {
            EntityVacuum vacuum = new EntityVacuum(caster.getEntityWorld(), target.hitVec, potencies[Element.AIR.ordinal()], potencies[Element.DEATH.ordinal()]);
            caster.getEntityWorld().spawnEntityInWorld(vacuum);
        }
        return false;
    }
}
