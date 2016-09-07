package com.teaminfinity.elementalinvocations.magic.spell.fire;

import com.infinityraider.infinitylib.utility.RayTraceHelper;
import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.souls.ISoulCollection;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.magic.spell.death.BasicSoul;
import com.teaminfinity.elementalinvocations.magic.spell.death.PlayerSoulCollectionProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class EffectDeathNova implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        int potencyFire = potencies[Element.FIRE.ordinal()];
        int potencyDeath = potencies[Element.DEATH.ordinal()];
        RayTraceResult hit = RayTraceHelper.getTargetEntityOrBlock(caster, 64);
        if(hit != null && hit.entityHit != null && (hit.entityHit instanceof EntityLivingBase)) {
            EntityLivingBase target = (EntityLivingBase) hit.entityHit;
            float relative = target.getMaxHealth() * (potencyDeath * 2.5F) / 100.0F;
            if(target.getHealth() < Math.min((float) potencyDeath, relative)) {
                Vec3d position = target.getPositionVector();
                target.setDead();
                //tnt = 4, creeper = 3, this goes from 1 to 5
                target.getEntityWorld().newExplosion(target, position.xCoord, position.yCoord, position.zCoord, potencyFire/3, true, potencyFire >= 12);
                ISoulCollection collection = PlayerSoulCollectionProvider.getSoulCollection(caster);
                if(collection != null) {
                    collection.addSoul(new BasicSoul(target.getName()));
                }
            }
        }
        return false;
    }
}
