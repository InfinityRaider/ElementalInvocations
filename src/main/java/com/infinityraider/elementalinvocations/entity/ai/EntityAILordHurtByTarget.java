package com.infinityraider.elementalinvocations.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILordHurtByTarget extends EntityAITarget {
    private EntityPlayer boss;
    private int timestamp;

    public EntityAILordHurtByTarget(EntityPlayer boss, EntityCreature entity) {
        super(entity, false);
        this.boss = boss;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (boss == null) {
            return false;
        } else {
            EntityLivingBase theOwnerAttacker = boss.getRevengeTarget();
            int i = boss.getRevengeTimer();
            return i != this.timestamp && this.isSuitableTarget(theOwnerAttacker, false);
        }

    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        if (boss != null) {
            this.timestamp = boss.getRevengeTimer();
        }
        super.startExecuting();
    }
}
