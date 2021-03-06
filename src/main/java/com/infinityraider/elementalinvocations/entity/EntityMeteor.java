package com.infinityraider.elementalinvocations.entity;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.magic.MagicDamageHandler;
import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.elementalinvocations.registry.SoundRegistry;
import com.infinityraider.elementalinvocations.render.entity.RenderEntityMeteor;
import com.infinityraider.infinitylib.sound.ModSoundHandler;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import com.infinityraider.infinitylib.utility.AreaHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
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
    private int potencyEarth;

    @SuppressWarnings("unused")
    public EntityMeteor(World world) {
        super(world);
    }

    public EntityMeteor(EntityPlayer caster, int potencyFire, int potencyEarth) {
        super(caster);
        this.potencyFire = potencyFire;
        this.potencyEarth = potencyEarth;
        Vec3d look = caster.getLookVec();
        this.posX = caster.posX - look.x * 50;
        this.posY = caster.posY + 100;
        this.posZ = caster.posZ - look.z * 50;
        this.shoot(look.x, 0, look.z, 5.0F, 0F);
    }

    public int getPotencyFire() {
        return potencyFire;
    }

    public int getPotencyEarth() {
        return potencyEarth;
    }

    public void channelUpdate(EntityPlayer caster) {
        RayTraceResult result = RayTraceHelper.getTargetBlock(caster, 64);
        if(result != null && result.hitVec != null) {
            Vec3d target = new Vec3d(result.hitVec.x - posX, 0, result.hitVec.z - posZ).normalize();
            Vec3d vOld = new Vec3d(motionX, 0, motionZ).normalize();
            double v = Math.sqrt(motionX*motionX + motionZ*motionZ);
            double x = 1.0 - 0.05*(getPotencyFire()/3);
            Vec3d vNew = new Vec3d(x*vOld.x + (1 - x)*target.x, 0, x*vOld.z + (1 - x)*target.z).scale(v);
            this.motionX = vNew.x;
            this.motionZ = vNew.z;
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0.5F;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if(getEntityWorld().isRemote) {
            spawnSmokeParticles();
        }
        AxisAlignedBB area = AreaHelper.getArea(result.hitVec, 3 + getPotencyEarth() /3);
        List<EntityLivingBase> entities = getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, area);
        entities.forEach(e ->
                MagicDamageHandler.getInstance().dealDamage(e, 2*getPotencyFire(), this, Element.FIRE, this.getPotencyFire(), this.getDirection())
        );
        if(!this.getEntityWorld().isRemote) {
            ModSoundHandler.getInstance().playSoundAtEntityOnce(this, SoundRegistry.getInstance().SOUND_METEOR, SoundCategory.PLAYERS);
        }
        this.setDead();
    }

    @SideOnly(Side.CLIENT)
    public void spawnFireParticles() {
        int r = 3 + getPotencyEarth() / 3;
        int delta = 40;
        double f = 0;
        for(int phi = 0 ; phi < 360 ; phi = phi + delta) {
            for(int theta = 0; theta < 180; theta = theta + delta) {
                double t = Math.toRadians(theta);
                double p = Math.toRadians(phi);
                double x = r * Math.cos(t) * Math.sin(p);
                double y = r * Math.sin(t);
                double z = r * Math.cos(t) * Math.cos(p);
                Minecraft.getMinecraft().renderGlobal.spawnParticle(
                        EnumParticleTypes.FLAME.getParticleID(), true,
                        posX + x, posY + y, posZ + z,
                        f * motionX, f * motionY, f * motionZ, 10);

            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnSmokeParticles() {
        int r = 3 + potencyEarth /3;
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
    protected NBTTagCompound writeDataToNBT(NBTTagCompound tag) {
        tag.setInteger(Names.NBT.LEVEL, this.potencyFire);
        tag.setInteger(Names.NBT.COUNT, this.potencyEarth);
        return tag;
    }

    @Override
    protected void readDataFromNBT(NBTTagCompound tag) {
        this.potencyFire = tag.getInteger(Names.NBT.LEVEL);
        this.potencyEarth = tag.getInteger(Names.NBT.COUNT);
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
}
