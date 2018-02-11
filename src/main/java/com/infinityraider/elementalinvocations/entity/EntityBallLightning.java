package com.infinityraider.elementalinvocations.entity;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.handler.DamageHandler;
import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.elementalinvocations.render.entity.RenderEntityBallLightning;
import com.infinityraider.infinitylib.modules.playerstate.ModulePlayerState;
import com.infinityraider.infinitylib.utility.DamageDealer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBallLightning extends EntityThrowableMagic {
    private static final AxisAlignedBB BOX = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
    private static final DamageDealer DMG = DamageHandler.getInstance().getDamageDealer(Element.AIR);

    private int potencyAir;
    private int potencyWater;
    private int timer;

    @SuppressWarnings("unused")
    public EntityBallLightning(World world) {
        super(world);
    }

    public EntityBallLightning(EntityPlayer caster, int potencyAir, int potencyWater) {
        super(caster);
        this.potencyAir = potencyAir;
        this.potencyWater = potencyWater;
        this.setEntityBoundingBox(BOX);
        this.setThrowableHeading(getDirection().xCoord, getDirection().yCoord, getDirection().zCoord, 3F, 0.1F);
        ModulePlayerState.getInstance().getState(caster).setInvisible(true).setInvulnerable(true).setEthereal(true).setUndetectable(true);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if(result.entityHit != null && result.entityHit != this.getThrower()) {
            if(result.entityHit instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) result.entityHit;
                DMG.apply(entity, this, this.potencyAir);
            }
        }
        BlockPos pos = result.getBlockPos();
        if(pos != null) {
            IBlockState state = getEntityWorld().getBlockState(pos);
            if (!state.getBlock().isAir(state, getEntityWorld(), pos)) {
                this.setDead();
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!this.getEntityWorld().isRemote) {
            timer = timer + 1;
            if(timer > potencyWater * 4) {
                this.setDead();
            }
        }
    }

    @Override
    public void setDead() {
        EntityPlayer caster = this.getThrower();
        if(caster != null) {
            caster.dismountRidingEntity();
            ModulePlayerState.getInstance().getState(caster).setInvisible(false).setInvulnerable(false).setEthereal(false).setUndetectable(false);
        }
        super.setDead();
    }

    @Override
    protected NBTTagCompound writeDataToNBT(NBTTagCompound tag) {
        tag.setInteger(Names.NBT.LEVEL, this.potencyAir);
        tag.setInteger(Names.NBT.COUNT, this.potencyWater);
        return tag;
    }

    @Override
    protected void readDataFromNBT(NBTTagCompound tag) {
        this.potencyAir = tag.getInteger(Names.NBT.LEVEL);
        this.potencyWater = tag.getInteger(Names.NBT.COUNT);
    }

    public static class RenderFactory implements IRenderFactory<EntityBallLightning> {
        private static final EntityBallLightning.RenderFactory INSTANCE = new EntityBallLightning.RenderFactory();

        public static EntityBallLightning.RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @SideOnly(Side.CLIENT)
        public Render<? super EntityBallLightning> createRenderFor(RenderManager manager) {
            return new RenderEntityBallLightning(manager);
        }
    }
}
