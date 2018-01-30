package com.infinityraider.elementalinvocations.entity;

import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.elementalinvocations.render.entity.RenderEntityMagicProjectile;
import com.infinityraider.elementalinvocations.magic.generic.MagicEffect;
import com.infinityraider.elementalinvocations.utility.ColorHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityMagicProjectile extends EntityThrowableMagic {
    private static final AxisAlignedBB BOX = new AxisAlignedBB(0, 0, 0, 0.25, 0.25, 0.25);

    private int[] potencies;
    private int timer;
    private int red = -1;
    private int green = -1;
    private int blue = -1;

    @SuppressWarnings("unused")
    public EntityMagicProjectile(World world) {
        super(world);
        this.setEntityBoundingBox(BOX);
    }

    public EntityMagicProjectile(EntityPlayer caster, int[] potencies) {
        super(caster);
        this.potencies = potencies;
        this.setEntityBoundingBox(BOX);
        this.setThrowableHeading(getDirection().xCoord, getDirection().yCoord, getDirection().zCoord, 2F, 0.5F);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!this.getEntityWorld().isRemote) {
            timer = timer + 1;
            if(timer > 5000) {
                this.setDead();
            }
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if(!worldObj.isRemote) {
            if(result.entityHit != null && (result.entityHit instanceof EntityLivingBase)) {
                new MagicEffect(getThrower(), (EntityLivingBase) result.entityHit, getDirection(), potencies).apply();
            }
        }
        this.setDead();
    }

    @Override
    protected float getGravityVelocity() {
        return 0.05F;
    }

    public int getRed() {
        if(red < 0) {
            compileColors();
        }
        return red;
    }

    public int getGreen() {
        if(green < 0) {
            compileColors();
        }
        return green;
    }

    public int getBlue() {
        if(blue < 0) {
            compileColors();
        }
        return blue;
    }

    private void compileColors() {
        int[] colors = ColorHelper.compileColors(this.potencies);
        this.red = colors[0];
        this.green = colors[1];
        this.blue = colors[2];
    }

    @Override
    protected NBTTagCompound writeDataToNBT(NBTTagCompound tag) {
        tag.setIntArray(Names.NBT.CHARGE, potencies);
        return tag;
    }

    @Override
    protected void readDataFromNBT(NBTTagCompound tag) {
        this.potencies = tag.getIntArray(Names.NBT.CHARGE);
    }

    public static class RenderFactory implements IRenderFactory<EntityMagicProjectile> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @SideOnly(Side.CLIENT)
        public Render<? super EntityMagicProjectile> createRenderFor(RenderManager manager) {
            return new RenderEntityMagicProjectile(manager);
        }
    }
}
