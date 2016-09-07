package com.teaminfinity.elementalinvocations.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityMoveHelper;

public class EntityMoveHelperTest extends EntityMoveHelper {
    public EntityMoveHelperTest(EntityLiving entity) {
        super(entity);
    }

    @Override
    public void onUpdateMoveHelper() {
        if(this.posX != this.entity.posX || this.posY != this.entity.posY || this.posZ != this.entity.posZ) {
            boolean breakpoint = true;
        }
        super.onUpdateMoveHelper();
    }
}
