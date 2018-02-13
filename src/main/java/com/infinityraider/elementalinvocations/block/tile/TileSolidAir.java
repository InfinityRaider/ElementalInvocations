package com.infinityraider.elementalinvocations.block.tile;

import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.magic.spell.earth.EffectSolidAir;
import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.infinitylib.block.tile.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class TileSolidAir extends TileEntityBase implements ITickable {
    private UUID ownerId;
    private int timer;

    @Override
    public void update() {
        if(!this.isRemote()) {
            if(this.timer <= 0) {
                this.clearWholeBarrier();
            }
            this.timer--;
        }
    }

    public UUID getOwnerId() {
        return this.ownerId;
    }

    public void clearWholeBarrier() {
        if(!this.isRemote()) {
            this.removeThisBarrier();
            BlockPos pos = this.getPos();
            for(EnumFacing facing : EnumFacing.values()) {
                TileEntity te = this.getWorld().getTileEntity(pos.offset(facing));
                if(te instanceof TileSolidAir) {
                    ((TileSolidAir) te).clearWholeBarrier();
                }
            }
        }
    }

    public void removeThisBarrier() {
        if(!this.isRemote()) {
            BlockPos pos = this.getPos();
            this.getWorld().setBlockToAir(pos);
            this.getWorld().removeTileEntity(pos);
            EffectSolidAir.removeBarrier(this);
        }
    }

    public TileSolidAir setOwner(EntityPlayer player) {
        if(!this.isOwner(player)) {
            this.ownerId = player.getUniqueID();
            if(!this.isRemote()) {
                this.markForUpdate();
            }
        }
        return this;
    }

    public TileSolidAir setTimer(int timer) {
        if(!this.isRemote()) {
            this.timer = timer;
        }
        return this;
    }

    public boolean isOwner(EntityPlayer player) {
        return player != null && player.getUniqueID().equals(this.ownerId);
    }

    public boolean isVisible() {
        return this.isOwner(ElementalInvocations.proxy.getClientPlayer());
    }

    @Override
    protected void writeTileNBT(NBTTagCompound tag) {
        tag.setString(Names.NBT.PLAYER, this.ownerId == null ? "null" : this.ownerId.toString());
        tag.setInteger(Names.NBT.COUNT, this.timer);
    }

    @Override
    protected void readTileNBT(NBTTagCompound tag) {
        String id = tag.hasKey(Names.NBT.PLAYER) ? tag.getString(Names.NBT.PLAYER) : "null";
        this.ownerId = id.equals("null") ? null : UUID.fromString(id);
        this.timer = tag.hasKey(Names.NBT.COUNT) ? tag.getInteger(Names.NBT.COUNT) : 0;
    }
}