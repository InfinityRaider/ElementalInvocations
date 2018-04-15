package com.infinityraider.elementalinvocations.entity;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.magic.MagicDamageHandler;
import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.elementalinvocations.registry.SoundRegistry;
import com.infinityraider.elementalinvocations.render.entity.RenderEntityWaveForm;
import com.infinityraider.infinitylib.modules.playerstate.ModulePlayerState;
import com.infinityraider.infinitylib.sound.ModSoundHandler;
import com.infinityraider.infinitylib.sound.SoundTask;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
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

    private SoundTask sound;

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
        this.posX = caster.posX;
        this.posY = caster.posY;
        this.posZ = caster.posZ;
        this.prevPosX = caster.posX;
        this.prevPosY = caster.posY;
        this.prevPosZ = caster.posZ;
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
    }

    @Override
    protected float getGravityVelocity() {
        return 0.25F;
    }

    public void channelUpdate(EntityPlayer caster) {
        Vec3d direction = caster.getLookVec();
        caster.extinguish();
        this.setThrowableHeading(direction.xCoord, direction.yCoord, direction.zCoord, 0.95F, 0.1F);
        this.channeled = true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!this.getEntityWorld().isRemote) {
            if(getThrower() == null || !this.channeled) {
                this.setDead();
            }
            this.getThrower().extinguish();
            this.extinguishBlock();
            this.channeled = false;
        }
    }

    protected void extinguishBlock() {
        if(!this.extinguishBlock(this.getPosition())) {
            this.extinguishBlock(this.getPosition().down());
        }
    }

    protected boolean extinguishBlock(BlockPos pos) {
        World world = this.getEntityWorld();
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() == Blocks.FLOWING_LAVA) {
            world.setBlockState(pos, Blocks.STONE.getDefaultState());
            return true;
        }
        if(state.getBlock() == Blocks.LAVA) {
            world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
            return true;
        }
        if(state.getBlock() == Blocks.FIRE) {
            world.setBlockToAir(pos);
            return true;
        }
        return false;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if(result.entityHit != null && result.entityHit != this.getThrower()) {
            if(result.entityHit instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) result.entityHit;
                MagicDamageHandler.getInstance().dealDamage(entity, ((float) this.potencyWater)/2.5F, this, Element.WATER, this.potencyWater, this.getDirection());
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
            ModulePlayerState.getInstance().getState(caster).setInvisible(false).setInvulnerable(false).setEthereal(false);
        }
        if(!this.getEntityWorld().isRemote) {
            this.stopPlayingSound();
        }
        super.setDead();
    }

    public void startPlayingSound() {
        if(!this.getEntityWorld().isRemote && this.sound == null && this.getThrower() != null) {
            this.sound = ModSoundHandler.getInstance().playSoundAtEntityContinuous(this.getThrower(), SoundRegistry.getInstance().SOUND_WAVEFORM, SoundCategory.PLAYERS);
        }
    }

    public void stopPlayingSound() {
        if(!this.getEntityWorld().isRemote && this.sound != null) {
            this.sound.stop();
            this.sound = null;
        }
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
}
