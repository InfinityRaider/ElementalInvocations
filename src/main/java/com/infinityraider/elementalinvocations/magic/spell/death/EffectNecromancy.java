package com.infinityraider.elementalinvocations.magic.spell.death;

import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.souls.ISoulCollection;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.entity.ai.EntityAIFollowLord;
import com.infinityraider.elementalinvocations.entity.ai.EntityAILordHurtByTarget;
import com.infinityraider.elementalinvocations.entity.ai.EntityAILordHurtTarget;
import com.infinityraider.elementalinvocations.registry.SoundRegistry;
import com.infinityraider.infinitylib.sound.ModSoundHandler;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class EffectNecromancy implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        ISoulCollection collection = CapabilityPlayerSoulCollection.getSoulCollection(caster);
        if(collection != null) {
            int amount = Math.min(collection.getSoulCount(), potencies.getPotency(Element.DEATH)/3);
            List<EntityMob> summons = new ArrayList<>();
            for(int i = 0; i < amount; i++) {
                collection.removeSoul();
                EntityMob mob;
                if(caster.getRNG().nextBoolean()) {
                    mob = new EntitySkeleton(caster.getEntityWorld());
                } else {
                    mob = new EntityZombie(caster.getEntityWorld());
                    mob.tasks.addTask(2, new EntityAIRestrictSun(mob));
                    mob.tasks.addTask(3, new EntityAIFleeSun(mob, 1.0D));
                    mob.tasks.taskEntries.removeIf(entry -> entry.action instanceof EntityAIMoveThroughVillage);
                }
                //override targeting ai
                mob.tasks.addTask(6, new EntityAIFollowLord(caster, mob, 2.0D, 10.0F, 2.0F));
                mob.targetTasks.taskEntries.clear();
                mob.targetTasks.addTask(1, new EntityAILordHurtByTarget(caster, mob));
                mob.targetTasks.addTask(2, new EntityAILordHurtTarget(caster, mob));
                mob.targetTasks.addTask(3, new EntityAIHurtByTarget(mob, true));
                //set mob position
                double x = caster.posX + (caster.getRNG().nextBoolean() ? -1 : 1) *(caster.getRNG().nextInt(3) + 1);
                double y = caster.posY;
                double z = caster.posZ + (caster.getRNG().nextBoolean() ? -1 : 1) *(caster.getRNG().nextInt(3) + 1);
                mob.onInitialSpawn(caster.getEntityWorld().getDifficultyForLocation(new BlockPos(mob)), null);
                mob.setPositionAndRotation(x, y ,z, caster.rotationYaw, caster.rotationPitch);
                mob.enablePersistence();
                //increase mob damage
                float modifier = ((float) potencies.getPotency(Element.EARTH))/20.0F;
                IAttributeInstance health = mob.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
                health.setBaseValue(health.getBaseValue() * (1 + modifier));
                IAttributeInstance dmg = mob.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
                dmg.setBaseValue(dmg.getBaseValue() * (1 + modifier));
                summons.add(mob);
            }
            summons.forEach(m -> caster.getEntityWorld().spawnEntity(m));
            if(summons.size() > 0) {
                ModSoundHandler.getInstance().playSoundAtEntityOnce(caster, SoundRegistry.getInstance().SOUND_NECROMANCY, SoundCategory.PLAYERS);
            }
        }
        return false;
    }
}
