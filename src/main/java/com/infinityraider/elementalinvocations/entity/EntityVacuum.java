package com.infinityraider.elementalinvocations.entity;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.handler.DamageHandler;
import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.elementalinvocations.render.entity.RenderEntityVacuum;
import com.infinityraider.elementalinvocations.utility.AreaHelper;
import com.infinityraider.infinitylib.utility.DamageDealer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class EntityVacuum extends Entity implements IEntityAdditionalSpawnData {
    private static final DamageDealer DMG = DamageHandler.getInstance().getDamageDealer(Element.DEATH);
    private static final int TIME = 5;

    private int potencyAir;
    private int potencyDeath;

    private int timer;

    private List<Entity> entities;

    public EntityVacuum(World world) {
        super(world);
        this.timer = TIME;
    }
    public EntityVacuum(World world, Vec3d position, int potencyAir, int potencyDeath) {
        this(world);
        this.potencyAir = potencyAir;
        this.potencyDeath = potencyDeath;
        this.posX = position.xCoord;
        this.posY = position.yCoord;
        this.posZ = position.zCoord;
        this.prevPosX = position.xCoord;
        this.prevPosY = position.yCoord;
        this.prevPosZ = position.zCoord;
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
    }

    public int getPotencyAir() {
        return this.potencyAir;
    }

    public int getPotencyDeath() {
        return this.potencyDeath;
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if(entities == null) {
            this.findEntities();
        }
        this.attractEntities();
        this.timer = this.timer - 1;
        if((this.timer <= 0 || this.entities.isEmpty()) && !this.getEntityWorld().isRemote) {
            this.setDead();
        }
    }

    @Override
    public void setDead() {
        this.entities.stream().filter(entity -> entity instanceof EntityLivingBase).forEach(entity ->
                DMG.applyDamage((EntityLivingBase) entity, this, this.getPotencyDeath())
        );
        super.setDead();
    }

    private void findEntities() {
        this.entities = new ArrayList<>();
        AxisAlignedBB area = AreaHelper.getArea(this.getPositionVector(), (this.getPotencyAir()/3 + 4)/2);
        List<Entity> targets = this.worldObj.getEntitiesWithinAABB(Entity.class, area);
        for(Entity e : targets) {
            if(e == this) {
                continue;
            }
            if(e instanceof EntityPlayer) {
                if(this.getEntityWorld().isRemote) {
                    this.entities.add(e);
                }
            } else if(!this.getEntityWorld().isRemote) {
                this.entities.add(e);
            }
        }
    }

    private void attractEntities() {
        for(Entity e : this.entities) {
            if(e instanceof EntityPlayer) {
                if(e.getEntityWorld().isRemote) {
                    this.attractEntity(e);
                }
            } else if(!e.getEntityWorld().isRemote) {
                this.attractEntity(e);
            }
        }
    }

    private void attractEntity(Entity e) {
        Vec3d m = this.getPositionVector();
        Vec3d p = e.getPositionVector();
        e.motionX = -(p.xCoord - m.xCoord)/2;
        e.motionY = -(p.yCoord - m.yCoord)/2;
        e.motionZ = -(p.zCoord - m.zCoord)/2;
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.potencyDeath = compound.getInteger(Names.NBT.LEVEL);
        this.potencyAir = compound.getInteger(Names.NBT.COUNT);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger(Names.NBT.LEVEL, this.potencyDeath);
        compound.setInteger(Names.NBT.COUNT, this.potencyAir);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(this.potencyDeath);
        buffer.writeInt(this.potencyAir);
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        this.potencyDeath = buffer.readInt();
        this.potencyAir = buffer.readInt();
    }


    public static class RenderFactory implements IRenderFactory<EntityVacuum> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @SideOnly(Side.CLIENT)
        public Render<? super EntityVacuum> createRenderFor(RenderManager manager) {
            return new RenderEntityVacuum(manager);
        }
    }
}
