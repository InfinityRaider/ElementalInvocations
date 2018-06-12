package com.teaminfinity.elementalinvocations.magic.spell.fire;

import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import com.teaminfinity.elementalinvocations.entity.EntityMeteor;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

public class EffectConjureMeteor implements ISpellEffect {
    private final Map<EntityPlayer, EntityMeteor> meteors;

    public EffectConjureMeteor() {
        this.meteors = new HashMap<>();
    }

    @Override
    public boolean apply(EntityPlayer caster, int[] potencies, int channelTick) {
        int potencyFire = potencies[Element.FIRE.ordinal()];
        int potencyEarth = potencies[Element.EARTH.ordinal()];
        if(channelTick == 0) {
            EntityMeteor meteor = new EntityMeteor(caster, potencyFire, potencyEarth);
            caster.getEntityWorld().spawnEntity(meteor);
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