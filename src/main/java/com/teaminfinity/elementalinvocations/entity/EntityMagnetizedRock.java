package com.teaminfinity.elementalinvocations.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityMagnetizedRock extends EntityThrowableMagic {
    public EntityMagnetizedRock(World world) {
        super(world);
    }

    public EntityMagnetizedRock(EntityPlayer caster, int index, int potency) {
        super(caster);
    }

    @Override
    protected NBTTagCompound writeDataToNBT(NBTTagCompound tag) {
        return null;
    }

    @Override
    protected void readDataFromNBT(NBTTagCompound tag) {

    }

    @Override
    protected void onImpact(RayTraceResult result) {

    }
}
