package com.teaminfinity.elementalinvocations.entity;

import com.teaminfinity.elementalinvocations.network.MessageRenderSunstrike;
import com.teaminfinity.elementalinvocations.reference.Names;
import com.teaminfinity.elementalinvocations.render.entity.RenderEntitySunstrike;
import com.teaminfinity.elementalinvocations.utility.AreaHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntitySunstrike extends Entity implements IEntityAdditionalSpawnData {
    private int timer;
    private int potencyFire;
    private int potencyAir;

    @SideOnly(Side.CLIENT)
    private boolean shouldRender;

    public EntitySunstrike(World world) {
        super(world);
        this.timer = 60;
    }

    public EntitySunstrike(World world, double x, double z, int potencyFire, int potencyAir) {
        this(world);
        this.posX = x;
        this.posZ = z;
        this.posY = getYForXZ(x, z);
        this.timer = 60;
        this.potencyFire = potencyFire;
        this.potencyAir = potencyAir;
    }

    private double getYForXZ(double x, double z) {
        int min = 0;
        BlockPos pos = new BlockPos(x, 255, z);
        IBlockState state = getEntityWorld().getBlockState(pos);
        while (state.getBlock().isAir(state, getEntityWorld(), pos) && pos.getY() > min) {
            pos = pos.down();
            state = getEntityWorld().getBlockState(pos);
        }
        return pos.getY() + 1;
    }

    public int getPotencyFire() {
        return this.potencyFire;
    }

    public int getPotencyAir() {
        return this.potencyAir;
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        timer = timer - 1;
        if (!this.getEntityWorld().isRemote) {
            if (timer == 0) {
                new MessageRenderSunstrike(this).sendToAll();
                AxisAlignedBB area = AreaHelper.getArea(this.getPositionVector(), Math.max(1, getPotencyAir() / 3));
                List<EntityLivingBase> entities = getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, area);
                entities.forEach(e -> e.attackEntityFrom(new DamageSourceSunstrike(), getPotencyFire() * 2));
            }
            if (timer <= -10) {
                this.setDead();
            }
        } else {
            if(timer == 0) {
                this.setShouldRender();
            }
        }
    }

    @Override
    protected void entityInit() {

    }

    @SideOnly(Side.CLIENT)
    public void setShouldRender() {
        this.shouldRender = true;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldRender() {
        return shouldRender;
    }


    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.potencyFire = compound.getInteger(Names.NBT.LEVEL);
        this.potencyAir = compound.getInteger(Names.NBT.COUNT);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger(Names.NBT.LEVEL, this.potencyFire);
        compound.setInteger(Names.NBT.COUNT, this.potencyAir);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(this.potencyFire);
        buffer.writeInt(this.potencyAir);
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        this.potencyFire = buffer.readInt();
        this.potencyAir = buffer.readInt();
    }

    public static class DamageSourceSunstrike extends DamageSource {
        public DamageSourceSunstrike() {
            super("fire");
            this.setMagicDamage();
            this.setFireDamage();
            this.setDamageBypassesArmor();
        }
    }


    public static class RenderFactory implements IRenderFactory<EntitySunstrike> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @SideOnly(Side.CLIENT)
        public Render<? super EntitySunstrike> createRenderFor(RenderManager manager) {
            return new RenderEntitySunstrike(manager);
        }
    }
}
