package com.infinityraider.elementalinvocations.magic.spell.fire;

import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.entity.EntitySunstrike;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

public class EffectSunstrike implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        RayTraceResult result = RayTraceHelper.getTargetBlock(caster, 128);
        if(result == null) {
            return false;
        }
        caster.worldObj.spawnEntityInWorld(new EntitySunstrike(
                        caster.worldObj, result.hitVec.xCoord, result.hitVec.zCoord,
                        potencies[Element.FIRE.ordinal()], potencies[Element.AIR.ordinal()]));
        return false;
    }
}
