package com.teaminfinity.elementalinvocations.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityWaterSpout extends Entity implements IEntityAdditionalSpawnData {
    private EntityPlayer caster;
    private int potencyEarth;
    private int potencyWater;

    public EntityWaterSpout(World world) {
        super(world);
    }

    public EntityWaterSpout(EntityPlayer caster, double x, double y, double z, int potencyEarth, int potencyWater) {
        this(caster.getEntityWorld());
        this.caster = caster;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.potencyEarth = potencyEarth;
        this.potencyWater = potencyWater;
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {

    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {

    }
}
