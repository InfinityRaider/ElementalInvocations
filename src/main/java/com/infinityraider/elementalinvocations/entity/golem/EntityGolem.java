package com.infinityraider.elementalinvocations.entity.golem;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityGolem extends EntityMob implements IEntityAdditionalSpawnData {
    private int potency;

    public EntityGolem(World world) {
        super(world);
    }

    public int getPotency() {
        return this.potency;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {

    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {

    }

}