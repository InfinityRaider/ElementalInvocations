package com.infinityraider.elementalinvocations.block.tile;

import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.infinitylib.block.tile.TileEntityBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public class TileSolidAir extends TileEntityBase {
    private UUID ownerId;
    private int timer;

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