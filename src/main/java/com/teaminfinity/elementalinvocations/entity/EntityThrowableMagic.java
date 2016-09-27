package com.teaminfinity.elementalinvocations.entity;

import com.teaminfinity.elementalinvocations.reference.Names;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;

public abstract class EntityThrowableMagic extends EntityThrowable implements IEntityAdditionalSpawnData {
    private Vec3d dir;

    public EntityThrowableMagic(World world) {
        super(world);
        this.setSize(1F, 1F);
    }

    public EntityThrowableMagic(EntityPlayer caster) {
        super(caster.getEntityWorld(), caster);
        this.dir = caster.getLookVec();
        this.setSize(1F, 1F);
    }

    @Override
    @Nullable
    public final EntityPlayer getThrower() {
        return (EntityPlayer) super.getThrower();
    }

    public final Vec3d getDirection() {
        return dir;
    }

    @Override
    public final void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        this.writeToNBTExtra(tag);
    }

    @Override
    public final void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.readFromNBTExtra(tag);
    }

    private NBTTagCompound writeToNBTExtra(NBTTagCompound tag) {
        tag.setDouble(Names.NBT.X, this.dir.xCoord);
        tag.setDouble(Names.NBT.Y, this.dir.yCoord);
        tag.setDouble(Names.NBT.Z, this.dir.zCoord);
        return this.writeDataToNBT(tag);
    }

    protected abstract NBTTagCompound writeDataToNBT(NBTTagCompound tag);

    private void readFromNBTExtra(NBTTagCompound tag) {
        this.dir = new Vec3d(tag.getDouble(Names.NBT.X), tag.getDouble(Names.NBT.Y), tag.getDouble(Names.NBT.Z));
        this.readDataFromNBT(tag);
    }

    protected abstract void readDataFromNBT(NBTTagCompound tag);

    @Override
    public final void writeSpawnData(ByteBuf buffer) {
        ByteBufUtils.writeTag(buffer, this.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public final void readSpawnData(ByteBuf buffer) {
        this.readFromNBT(ByteBufUtils.readTag(buffer));
    }

}
