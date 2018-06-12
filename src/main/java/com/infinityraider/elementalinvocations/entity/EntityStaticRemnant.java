package com.infinityraider.elementalinvocations.entity;

import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.elementalinvocations.render.entity.RenderEntityStaticRemnant;
import com.infinityraider.infinitylib.network.serialization.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class EntityStaticRemnant extends Entity implements IEntityAdditionalSpawnData {
    private static final AxisAlignedBB HIT_BOX = new AxisAlignedBB(-1, 0, -1, 1, 2, 1);

    private EntityPlayer caster;
    private UUID casterUUID;

    private int air;
    private int life;

    private int timer;

    public EntityStaticRemnant(World world) {
        super(world);
    }

    public EntityStaticRemnant(EntityPlayer caster, int air, int life) {
        this(caster.getEntityWorld());
        this.caster = caster;
        this.casterUUID = caster.getUniqueID();
        this.air = air;
        this.life = life;
        this.posX = caster.posX;
        this.posY = caster.posY;
        this.posZ = caster.posZ;
    }

    @Override
    protected void entityInit() {
        this.timer = 1200*this.life;
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        this.timer = this.timer - 1;
        if(this.timer <= 0) {
            this.setDead();
        }
    }

    public EntityPlayer getCaster() {
        if(this.caster == null) {
            this.caster = this.getEntityWorld().getPlayerEntityByUUID(this.getCasterUniqueId());
        }
        return this.caster;
    }

    public UUID getCasterUniqueId() {
        return this.casterUUID;
    }

    @SideOnly(Side.CLIENT)
    public AbstractClientPlayer getClientPlayer() {
        if(this.getCaster() instanceof AbstractClientPlayer) {
            return (AbstractClientPlayer) this.getCaster();
        }
        return null;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        this.caster = null;
        this.casterUUID = nbt.hasKey(Names.NBT.PLAYER) ? UUID.fromString(nbt.getString(Names.NBT.PLAYER)) : null;
        this.caster = this.casterUUID == null ? null : this.getCaster();
        this.air = nbt.hasKey(Names.NBT.AIR) ?  nbt.getInteger(Names.NBT.AIR) : 0;
        this.life = nbt.hasKey(Names.NBT.LIFE) ? nbt.getInteger(Names.NBT.LIFE) : 0;
        this.timer = nbt.hasKey(Names.NBT.COUNT) ? nbt.getInteger(Names.NBT.COUNT) : 0;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setString(Names.NBT.PLAYER, this.getUniqueID().toString());
        nbt.setInteger(Names.NBT.AIR, this.air);
        nbt.setInteger(Names.NBT.LIFE, this.life);
        nbt.setInteger(Names.NBT.COUNT, this.timer);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        ByteBufUtil.writeString(buffer, this.getUniqueID().toString());
        buffer.writeInt(this.air);
        buffer.writeInt(this.life);
        buffer.writeInt(this.timer);
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        this.casterUUID = UUID.fromString(ByteBufUtil.readString(buffer));
        this.air = buffer.readInt();
        this.life = buffer.readInt();
        this.timer = buffer.readInt();
    }


    public static class RenderFactory implements IRenderFactory<EntityStaticRemnant> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @SideOnly(Side.CLIENT)
        public RenderEntityStaticRemnant createRenderFor(RenderManager manager) {
            return new RenderEntityStaticRemnant(manager);
        }
    }
}