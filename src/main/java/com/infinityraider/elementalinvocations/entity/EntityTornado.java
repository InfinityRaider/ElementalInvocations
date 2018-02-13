package com.infinityraider.elementalinvocations.entity;

import com.infinityraider.elementalinvocations.render.entity.RenderEntityMeteor;
import com.infinityraider.elementalinvocations.render.entity.RenderEntityTornado;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityTornado extends EntityThrowableMagic {
    int potencyAir;
    int potencyDeath;

    @SuppressWarnings("unused")
    public EntityTornado(World world) {
        super(world);
    }

    public EntityTornado(EntityPlayer caster, int potencyAir, int potencyDeath) {
        super(caster);
        this.potencyAir = potencyAir;
        this.potencyDeath = potencyDeath;
        Vec3d look = caster.getLookVec();
        this.posX = caster.posX + look.xCoord;
        this.posY = caster.posY;
        this.posZ = caster.posZ + look.zCoord;
        this.setThrowableHeading(look.xCoord, 0, look.zCoord, 0.0F, 0F);
    }

    public int getPotencyAir() {
        return potencyAir;
    }

    public int getPotencyDeath() {
        return potencyDeath;
    }

    @Override
    protected float getGravityVelocity() {
        return 0.0F;
    }

    @Override
    protected void onImpact(RayTraceResult result) {

    }

    @Override
    protected NBTTagCompound writeDataToNBT(NBTTagCompound tag) {

        return tag;
    }

    @Override
    protected void readDataFromNBT(NBTTagCompound tag) {

    }

    public static class RenderFactory implements IRenderFactory<EntityTornado> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @SideOnly(Side.CLIENT)
        public Render<? super EntityTornado> createRenderFor(RenderManager manager) {
            return new RenderEntityTornado(manager);
        }
    }
}