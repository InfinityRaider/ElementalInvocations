package com.teaminfinity.elementalinvocations.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILordHurtTarget extends EntityAITarget {
    private EntityPlayer boss;
    private EntityLivingBase theTarget;
    private int timestamp;

    public EntityAILordHurtTarget(EntityPlayer lord, EntityCreature entity) {
        super(entity, false);
        this.boss = lord;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (boss == null) {
            return false;
        } else {
            this.theTarget = boss.getLastAttacker();
            int i = boss.getLastAttackerTime();
            return i != this.timestamp && this.isSuitableTarget(this.theTarget, false);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.theTarget);
        if (boss != null) {
            this.timestamp = boss.getLastAttackerTime();
        }
        super.startExecuting();
    }
}
