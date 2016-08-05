package com.teaminfinity.elementalinvocations.entity;

import com.teaminfinity.elementalinvocations.handler.PlayerStateHandler;
import com.teaminfinity.elementalinvocations.reference.Names;
import com.teaminfinity.elementalinvocations.render.entity.RenderEntityWaveForm;
import com.teaminfinity.elementalinvocations.utility.LogHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWaveForm extends EntityThrowableMagic {
    private static final AxisAlignedBB BOX = new AxisAlignedBB(-0.5F, -0.5F, -0.5F, 1.5F, 1.5F, 1.5F);

    private int potencyWater;
    private boolean channeled;

    @SuppressWarnings("unused")
    public EntityWaveForm(World world) {
        super(world);
        this.setEntityBoundingBox(BOX);
        this.noClip = false;
    }
    public EntityWaveForm(EntityPlayer caster, int potencyWater) {
        super(caster);
        this.potencyWater = potencyWater;
        this.setEntityBoundingBox(BOX);
        this.noClip = false;
    }

    @Override
    protected float getGravityVelocity() {
        return 0.5F;
    }

    public void channelUpdate(EntityPlayer caster) {
        Vec3d direction = caster.getLookVec();
        this.setThrowableHeading(direction.xCoord, direction.yCoord, direction.zCoord, 0.95F, 0.1F);
        this.channeled = true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!this.getEntityWorld().isRemote) {
            if(!this.channeled) {
                this.setDead();
            }
            this.channeled = false;
        }
        LogHelper.debug("vertical velocity: " + motionY);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if(result.entityHit != null && result.entityHit != this.getThrower()) {
            if(result.entityHit instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) result.entityHit;
                entity.attackEntityFrom(new DamageSourceWaveForm(), ((float) this.potencyWater)/2.5F);
            }
        }
        BlockPos pos = result.getBlockPos();
        if(pos != null && result.sideHit != null) {
            switch(result.sideHit.getAxis()) {
                case X:
                    this.motionX = 0;
                    break;
                case Y:
                    this.motionY = 0;
                    LogHelper.debug("set vertical velocity to 0");
                    break;
                case Z:
                    this.motionZ = 0;
                    break;
            }
        }
    }

    @Override
    public void setDead() {
        EntityPlayer caster = this.getThrower();
        if(caster != null) {
            caster.dismountRidingEntity();
            PlayerStateHandler.getInstance().getState(caster).setInvisible(false).setInvulnerable(false).setEthereal(false);
        }
        super.setDead();
    }

    @Override
    protected NBTTagCompound writeDataToNBT(NBTTagCompound tag) {
        tag.setInteger(Names.NBT.LEVEL, potencyWater);
        return tag;
    }

    @Override
    protected void readDataFromNBT(NBTTagCompound tag) {
        this.potencyWater = tag.getInteger(Names.NBT.LEVEL);
    }

    public static class RenderFactory implements IRenderFactory<EntityWaveForm> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @SideOnly(Side.CLIENT)
        public Render<? super EntityWaveForm> createRenderFor(RenderManager manager) {
            return new RenderEntityWaveForm(manager);
        }
    }

    public static class DamageSourceWaveForm extends DamageSource {
        public DamageSourceWaveForm() {
            super("cold");
        }
    }
}
