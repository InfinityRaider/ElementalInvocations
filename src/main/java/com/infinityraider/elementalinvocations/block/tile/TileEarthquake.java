package com.infinityraider.elementalinvocations.block.tile;

import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.infinitylib.block.tile.TileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

import java.util.UUID;

public class TileEarthquake extends TileEntityBase implements ITickable {
    private static final int PERIOD = 10;

    private EntityPlayer caster;

    private UUID casterId;
    private IBlockState originalState;
    private int frame;

    public TileEarthquake() {
        super();
        this.frame = (int) (PERIOD*Math.random());
    }

    public IBlockState getOriginalState() {
        return this.originalState;
    }

    public TileEarthquake setOriginalState(IBlockState state) {
        this.originalState = state;
        this.markForUpdate();
        return this;
    }

    public EntityPlayer getCaster() {
        if(this.caster == null) {
            this.caster = this.getWorld().getPlayerEntityByUUID(this.casterId);
        }
        return this.caster;
    }

    public TileEarthquake setCaster(EntityPlayer player) {
        this.caster = player;
        this.casterId = player == null ? null : player.getUniqueID();
        return this;
    }

    public int getFrame() {
        return this.frame;
    }

    public int getMaxFrames() {
        return PERIOD;
    }

    @Override
    public void update() {
        this.frame = (frame+1) % PERIOD;
    }

    @Override
    protected void writeTileNBT(NBTTagCompound tag) {
        tag.setInteger(Names.NBT.COUNT, this.frame);
        tag.setString(Names.NBT.ELEMENT, this.getOriginalState().getBlock().getRegistryName().toString());
        tag.setInteger(Names.NBT.LEVEL, this.getOriginalState().getBlock().getMetaFromState(this.getOriginalState()));
        tag.setString(Names.NBT.PLAYER, this.casterId == null ? "null" : this.casterId.toString());
    }

    @Override
    protected void readTileNBT(NBTTagCompound tag) {
        this.frame = tag.hasKey(Names.NBT.COUNT) ? tag.getInteger(Names.NBT.COUNT) : (int) (PERIOD*Math.random());
        boolean flag = tag.hasKey(Names.NBT.ELEMENT);
        if(flag) {
            Block block = Block.getBlockFromName(tag.getString(Names.NBT.ELEMENT));
            if(block == null) {
                this.originalState = Blocks.STONE.getDefaultState();
            } else {
                int meta = tag.hasKey(Names.NBT.LEVEL) ? tag.getInteger(Names.NBT.LEVEL) : 0;
                this.originalState = block.getStateFromMeta(meta);
            }
        } else {
            this.originalState = Blocks.STONE.getDefaultState();
        }
        String uuid = tag.hasKey(Names.NBT.PLAYER) ? tag.getString(Names.NBT.PLAYER) : "null";
        this.casterId = uuid.equalsIgnoreCase("null") ? null : UUID.fromString(uuid);
    }
}