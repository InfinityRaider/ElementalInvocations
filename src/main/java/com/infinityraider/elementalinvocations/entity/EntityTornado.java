package com.infinityraider.elementalinvocations.entity;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.magic.MagicDamageHandler;
import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.elementalinvocations.registry.SoundRegistry;
import com.infinityraider.elementalinvocations.render.entity.RenderEntityTornado;
import com.infinityraider.infinitylib.sound.ModSoundHandler;
import com.infinityraider.infinitylib.sound.SoundTask;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

public class EntityTornado extends EntityThrowableMagic {
    private int potencyAir;
    private int potencyDeath;

    private int timer;

    private SoundTask sound;

    private Map<Entity, Boolean> suckedEntities;

    @SuppressWarnings("unused")
    public EntityTornado(World world) {
        super(world);
        this.setSize(this.getPotencyAir() / 3, 5);
        this.suckedEntities = new IdentityHashMap<>();
    }

    public EntityTornado(EntityPlayer caster, int potencyAir, int potencyDeath) {
        super(caster);
        this.potencyAir = potencyAir;
        this.potencyDeath = potencyDeath;
        this.setSize(this.getPotencyAir() / 3, 5);
        this.timer = this.getPotencyAir() * 20 / 3;
        this.suckedEntities = new IdentityHashMap<>();
        Vec3d look = caster.getLookVec();
        this.posX = caster.posX + look.x;
        this.posY = caster.posY;
        this.posZ = caster.posZ + look.z;
        this.shoot(look.x, 0, look.z, 1.0F, 0F);
    }

    public int getPotencyAir() {
        return potencyAir;
    }

    public int getPotencyDeath() {
        return potencyDeath;
    }

    @Override
    public void onUpdate() {
        if(!this.getEntityWorld().isRemote) {
            if(this.timer <= 0) {
                this.setDead();
            }
            this.timer--;
        }
        this.updateSuckedEntities();
        this.suckEntities();
        super.onUpdate();
    }

    protected void updateSuckedEntities() {
        getEntityWorld().getEntitiesWithinAABB(Entity.class, this.getEntityBoundingBox(),(e) -> e != this && e != this.getThrower())
                .forEach(entity -> this.suckedEntities.put(entity, true));
    }

    protected void suckEntities() {
        Iterator<Map.Entry<Entity, Boolean>> it = this.suckedEntities.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Entity, Boolean> entry = it.next();
            if(!entry.getValue()) {
                this.applyDamage(entry.getKey());
                it.remove();
            } else {
                entry.setValue(false);
                Entity e = entry.getKey();
                Vec3d m = this.getPositionVector();
                Vec3d p = e.getPositionVector();
                e.motionX = -(p.x - m.x)/2;
                e.motionY = 1;
                e.motionZ = -(p.z - m.z)/2;
            }
        }
    }

    protected void applyDamage(Entity e) {
        if(!this.getEntityWorld().isRemote && e instanceof EntityLivingBase) {
            MagicDamageHandler.getInstance().dealDamage(e, this.getPotencyDeath()/2, this.getThrower(), Element.DEATH, this.getPotencyDeath());
        }
    }

    @Override
    public void setDead() {
        if(!this.getEntityWorld().isRemote) {
            this.suckedEntities.forEach((e, b) -> this.applyDamage(e));
            this.stopPlayingSound();
        }
        this.suckedEntities.clear();
        super.setDead();
    }

    public void startPlayingSound() {
        if(!this.getEntityWorld().isRemote && this.sound == null) {
            this.sound = ModSoundHandler.getInstance().playSoundAtEntityOnce(this, SoundRegistry.getInstance().SOUND_TORNADO, SoundCategory.PLAYERS);
        }
    }

    public void stopPlayingSound() {
        if(!this.getEntityWorld().isRemote && this.sound != null) {
            this.sound.stop();
            this.sound = null;
        }
    }

    @Override
    protected float getGravityVelocity() {
        return 0.1F;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if(result == null) {
            return;
        }
        BlockPos pos = result.getBlockPos();
        if(pos != null && result.sideHit != null) {
            switch(result.sideHit.getAxis()) {
                case X:
                    this.motionY = this.motionX;
                    this.motionX = 0;
                    break;
                case Y:
                    this.motionY = 0;
                    break;
                case Z:
                    this.motionY = this.motionZ;
                    this.motionZ = 0;
                    break;
            }
            this.motionY = 0;
        }
    }

    @Override
    protected NBTTagCompound writeDataToNBT(NBTTagCompound tag) {
        tag.setInteger(Names.NBT.AIR, this.getPotencyAir());
        tag.setInteger(Names.NBT.DEATH, this.getPotencyDeath());
        tag.setInteger(Names.NBT.COUNT, this.timer);
        return tag;
    }

    @Override
    protected void readDataFromNBT(NBTTagCompound tag) {
        this.potencyAir = tag.hasKey(Names.NBT.AIR) ? tag.getInteger(Names.NBT.AIR) : 0;
        this.potencyDeath = tag.hasKey(Names.NBT.DEATH) ? tag.getInteger(Names.NBT.DEATH) : 0;
        this.timer = tag.hasKey(Names.NBT.COUNT) ? tag.getInteger(Names.NBT.COUNT) : 0;
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