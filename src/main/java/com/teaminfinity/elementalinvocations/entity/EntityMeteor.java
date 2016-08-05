package com.teaminfinity.elementalinvocations.entity;

import com.teaminfinity.elementalinvocations.reference.Names;
import com.teaminfinity.elementalinvocations.render.entity.RenderEntityMeteor;
import com.teaminfinity.elementalinvocations.utility.AreaHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntityMeteor extends EntityThrowableMagic {
    private int potencyFire;
    private int getPotencyEarth;

    @SuppressWarnings("unused")
    public EntityMeteor(World world) {
        super(world);
    }

    public EntityMeteor(EntityPlayer caster, int potencyFire, int potencyEarth) {
        super(caster);
        this.potencyFire = potencyFire;
        this.getPotencyEarth = potencyEarth;
        Vec3d look = caster.getLookVec();
        this.posX = caster.posX - look.xCoord * 50;
        this.posY = 100;
        this.posZ = caster.posZ - look.zCoord * 50;
        this.setThrowableHeading(look.xCoord, 0, look.zCoord, 2.5F, 0F);
    }

    public void channelUpdate(EntityPlayer caster) {
        Vec3d direction = caster.getLookVec();
        //this.setThrowableHeading(direction.xCoord, 0, direction.zCoord, 10F, 0F);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if(getEntityWorld().isRemote) {
            spawnParticles();
        }
        AxisAlignedBB area = AreaHelper.getArea(result.hitVec, 3 + getPotencyEarth/3);
        List<EntityLivingBase> entities = getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, area);
        entities.forEach(e -> e.attackEntityFrom(new DamageSourceMeteor(), 2 * potencyFire));
        this.setDead();
    }

    @SideOnly(Side.CLIENT)
    private void spawnParticles() {
        int r = 3 + getPotencyEarth/3;
        for(int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    int radius = x * x + y * y + z * z;
                    if (radius > r * r) {
                        continue;
                    }
                    BlockPos pos = getPosition().add(x, y, z);
                    Minecraft.getMinecraft().renderGlobal.spawnParticle(
                            EnumParticleTypes.SMOKE_LARGE.getParticleID(), true,
                            pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5D,
                            ((double) x)/30.0D, ((double) y)/30.0D, ((double) z)/30.0D,
                            50
                    );
                }
            }
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0.1F;
    }

    @Override
    protected NBTTagCompound writeDataToNBT(NBTTagCompound tag) {
        tag.setInteger(Names.NBT.LEVEL, this.potencyFire);
        tag.setInteger(Names.NBT.COUNT, this.getPotencyEarth);
        return tag;
    }

    @Override
    protected void readDataFromNBT(NBTTagCompound tag) {
        this.potencyFire = tag.getInteger(Names.NBT.LEVEL);
        this.getPotencyEarth = tag.getInteger(Names.NBT.COUNT);
    }
    public static class RenderFactory implements IRenderFactory<EntityMeteor> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @SideOnly(Side.CLIENT)
        public Render<? super EntityMeteor> createRenderFor(RenderManager manager) {
            return new RenderEntityMeteor(manager);
        }
    }

    public static class DamageSourceMeteor extends DamageSource {
        public DamageSourceMeteor() {
            super("cold");
        }
    }

}