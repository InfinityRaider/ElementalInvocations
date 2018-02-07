package com.infinityraider.elementalinvocations.magic.spell.fire;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.entity.EntityMeteor;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

public class EffectConjureMeteor implements ISpellEffect {
    private final Map<EntityPlayer, EntityMeteor> meteors;

    public EffectConjureMeteor() {
        this.meteors = new HashMap<>();
    }

    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        int potencyFire = potencies.getPotency(Element.FIRE);
        int potencyEarth = potencies.getPotency(Element.EARTH);
        if(channelTick == 0) {
            EntityMeteor meteor = new EntityMeteor(caster, potencyFire, potencyEarth);
            caster.worldObj.spawnEntityInWorld(meteor);
            meteors.put(caster, meteor);
        }
        EntityMeteor meteor = meteors.get(caster);
        if(meteor == null) {
            return false;
        }
        if(meteor.isEntityAlive()) {
            meteor.channelUpdate(caster);
            return true;
        }
        return false;
    }
}
