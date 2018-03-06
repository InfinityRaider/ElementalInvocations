package com.infinityraider.elementalinvocations.block.tile;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.magic.MagicDamageHandler;
import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.infinitylib.block.tile.TileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class TileEarthquake extends TileEntityBase implements ITickable {
    public static final int PERIOD = 10;
    public static final double AMPLITUDE = 0.25;
    public static final int DURATION = 100;
    public static final double FORCE = 0.5;

    public static final Vec3d DIR = new Vec3d(0, 1, 0);
    public static final AxisAlignedBB[] HIT_BOXES;

    private EntityPlayer caster;
    private int potency;

    private UUID casterId;
    private IBlockState originalState;
    private int frame;
    private int timer;

    public TileEarthquake() {
        super();
        this.frame = (int) (PERIOD*Math.random());
        this.timer = DURATION;
    }

    public Block getOriginalBlock() {
        return this.getOriginalState().getBlock();
    }

    public IBlockState getOriginalState() {
        return this.originalState == null ? Blocks.AIR.getDefaultState() : this.originalState;
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

    public int getPotency() {
        return this.potency;
    }

    public TileEarthquake setPotency(int potency) {
        this.potency = potency;
        return this;
    }

    public int getFrame() {
        return this.frame;
    }

    public int getMaxFrames() {
        return PERIOD;
    }

    public AxisAlignedBB getBoundingBox() {
        return HIT_BOXES[this.getFrame()].offset(this.xCoord(), this.yCoord(), this.zCoord());
    }

    @Override
    public void update() {
        if(this.timer < 0) {
            this.finish();
        } else {
            this.frame = (frame + 1) % PERIOD;
            this.timer--;
        }
    }

    public void affectEntity(Entity entity) {
        if(!this.getWorld().isRemote && (entity instanceof EntityLivingBase) && entity != this.getCaster()) {
            MagicDamageHandler.getInstance().dealDamage(entity, 2, this.getCaster(), Element.EARTH, this.getPotency(), DIR);
        }
        entity.motionX = (FORCE/2) - FORCE*this.getRandom().nextDouble();
        entity.motionY = FORCE;
        entity.motionZ = (FORCE/2) - FORCE*this.getRandom().nextDouble();
    }

    public void finish() {
        if(!this.isRemote()) {
            this.getWorld().setBlockState(this.getPos(), this.getOriginalState());
        }
    }

    @Override
    protected void writeTileNBT(NBTTagCompound tag) {
        tag.setInteger(Names.NBT.COUNT, this.frame);
        if(this.getOriginalState() != null) {
            tag.setString(Names.NBT.ELEMENT, this.getOriginalState().getBlock().getRegistryName().toString());
            tag.setInteger(Names.NBT.LEVEL, this.getOriginalState().getBlock().getMetaFromState(this.getOriginalState()));
        }
        tag.setString(Names.NBT.PLAYER, this.casterId == null ? "null" : this.casterId.toString());
        tag.setInteger(Names.NBT.CHARGE, this.timer);
        tag.setInteger(Names.NBT.EXPERIENCE, this.potency);
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
        this.timer = tag.hasKey(Names.NBT.CHARGE) ? tag.getInteger(Names.NBT.CHARGE) : 0;
        this.potency = tag.hasKey(Names.NBT.EXPERIENCE) ? tag.getInteger(Names.NBT.EXPERIENCE) : 0;
    }

    static {
        AxisAlignedBB[] boxes = new AxisAlignedBB[PERIOD];
        for(int i = 0; i < PERIOD; i++) {
            double dy = AMPLITUDE*Math.sin(2*Math.PI*i/PERIOD);
            boxes[i] = new AxisAlignedBB(0, 0, 0, 1, 1 + dy, 1);
        }
        HIT_BOXES = boxes;
    }
}