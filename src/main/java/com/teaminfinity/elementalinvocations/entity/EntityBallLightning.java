package com.teaminfinity.elementalinvocations.entity;

import com.teaminfinity.elementalinvocations.reference.Names;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityBallLightning extends EntityThrowableMagic{
    private static final AxisAlignedBB BOX = new AxisAlignedBB(0, 0, 0, 1, 1, 1);

    private int potencyAir;
    private int potencyWater;
    private int timer;

    @SuppressWarnings("unused")
    public EntityBallLightning(World world) {
        super(world);
    }

    public EntityBallLightning(EntityPlayer caster, int potencyAir, int potencyWater) {
        super(caster);
        this.potencyAir = potencyAir;
        this.potencyWater = potencyWater;
        this.setEntityBoundingBox(BOX);
        this.setThrowableHeading(getDirection().xCoord, getDirection().yCoord, getDirection().zCoord, 2F, 0.1F);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if(result.entityHit != null && result.entityHit != this.getThrower()) {
            if(result.entityHit instanceof EntityLivingBase) {

            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!this.getEntityWorld().isRemote) {
            timer = timer + 1;
            if(timer > potencyWater * 4) {
                this.setDead();
            }
        }
    }

    @Override
    protected NBTTagCompound writeDataToNBT(NBTTagCompound tag) {
        tag.setInteger(Names.NBT.LEVEL, this.potencyAir);
        tag.setInteger(Names.NBT.COUNT, this.potencyWater);
        return tag;
    }

    @Override
    protected void readDataFromNBT(NBTTagCompound tag) {
        this.potencyAir = tag.getInteger(Names.NBT.LEVEL);
        this.potencyWater = tag.getInteger(Names.NBT.COUNT);
    }
}
