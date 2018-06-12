package com.infinityraider.elementalinvocations.entity;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.magic.MagicDamageHandler;
import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.elementalinvocations.magic.spell.earth.EffectMagnetize;
import com.infinityraider.elementalinvocations.render.entity.RenderEntityMagnetizedRock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntityMagnetizedRock extends EntityThrowableMagic {
    /** The radius at which the rocks orbit the player */
    public static final float RADIUS = 2.5F;
    /** The distance the rocks move each tick */
    public static final float VELOCITY = 0.1F;

    private static final DataParameter<EnumStage> DATA_STAGE = EntityDataManager.createKey(EntityMagnetizedRock.class, EnumStage.dataSerializer);
    private static final DataParameter<Integer> DATA_HITS = EntityDataManager.createKey(EntityMagnetizedRock.class, DataSerializers.VARINT);

    private EffectMagnetize effect;
    private IBlockState block;
    private int potencyEarth;

    @SuppressWarnings("unused")
    public EntityMagnetizedRock(World world) {
        super(world);
    }

    public EntityMagnetizedRock(EntityPlayer caster, EffectMagnetize effect, IBlockState block, int potencyEarth) {
        super(caster);
        this.effect = effect;
        this.block = block;
        this.potencyEarth = potencyEarth;
        this.setStage(EnumStage.PULLING);
    }

    public int getPotencyEarth() {
        return this.potencyEarth;
    }

    public IBlockState getBlockState() {
        return this.block;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(DATA_STAGE, EnumStage.PULLING);
        this.getDataManager().register(DATA_HITS, 0);
    }

    public void throwStone(Vec3d direction) {
        this.shoot(direction.x, direction.y, direction.z, 2.5F, 0.1F);
        this.setStage(EnumStage.THROWN);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.1F;
    }

    @Override
    public void onUpdate() {
        if(!this.getEntityWorld().isRemote && this.effect == null) {
            this.setDead();
        } else {
            switch (this.getStage()) {
                case PULLING:
                    this.onPullInUpdate();
                    break;
                case ORBITING:
                    this.onOrbitingUpdate();
                    break;
                case THROWN:
                    super.onUpdate();
                    break;
            }
        }
    }

    protected void onPullInUpdate() {
        EntityPlayer player = this.getThrower();
        boolean flag = Math.abs(this.posY - (player.posY + player.height/2)) <= VELOCITY;
        if(flag) {
            double dx = player.posX - this.posX;
            double dy = player.posY + player.height/2 - this.posY;
            double dz = player.posZ - this.posZ;
            double delta = Math.sqrt(dx*dx + dz*dz) - RADIUS;
            Vec3d direction = new Vec3d(dx, 0, dz).normalize();
            double m = 1;
            if(delta > 20) {
                this.setDead();
                return;
            }
            if(delta > 2*VELOCITY) {
                m = 2*delta/3;
            }
            this.motionX = direction.x*(VELOCITY)*m*(delta > 0 ? 1 : -1);
            this.motionY = dy;
            this.motionZ = direction.z*(VELOCITY)*m*(delta > 0 ? 1 : -1);
        } else {
            this.motionX = 0;
            this.motionY = (VELOCITY) * ((player.posY + player.height/2) >= this.posY ? 1 : -1);
            this.motionZ = 0;
        }
        this.updateMovement(false);
        if(flag) {
            double distX =  player.posX - this.posX;
            double distZ =  player.posZ - this.posZ;
            double delta = distX*distX + distZ*distZ - RADIUS*RADIUS;
            if(delta <= VELOCITY*VELOCITY) {
                this.setStage(EnumStage.ORBITING);
            }
        }
    }

    protected void onOrbitingUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        this.onEntityUpdate();
        this.calculateVelocityForOrbit();
        this.updateMovement(true);
    }

    protected void updateMovement(boolean canCollide) {
        Vec3d oldPosition = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d nextPosition = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if(canCollide) {
            RayTraceResult hit = this.getEntityWorld().rayTraceBlocks(oldPosition, nextPosition);
            oldPosition = new Vec3d(this.posX, this.posY, this.posZ);
            if (hit != null) {
                nextPosition = new Vec3d(hit.hitVec.x, hit.hitVec.y, hit.hitVec.z);
            } else {
                nextPosition = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            }
            Entity entity = null;
            List<Entity> list = this.getEntityWorld().getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(this.motionX, this.motionY, this.motionZ).grow(1.0D));
            double d0 = 0.0D;
            for (Entity entityAt : list) {
                if (entityAt.canBeCollidedWith()) {
                    if (this.ticksExisted < 2 && this.ignoreEntity == null) {
                        this.ignoreEntity = entityAt;
                    } else {
                        AxisAlignedBB axisalignedbb = entityAt.getEntityBoundingBox().grow(0.30000001192092896D);
                        RayTraceResult hitEntity = axisalignedbb.calculateIntercept(oldPosition, nextPosition);
                        if (hitEntity != null) {
                            double d1 = oldPosition.squareDistanceTo(hitEntity.hitVec);
                            if (d1 < d0 || d0 == 0.0D) {
                                entity = entityAt;
                                d0 = d1;
                            }
                        }
                    }
                }
            }
            if (entity != null) {
                hit = new RayTraceResult(entity);
            }
            if (hit != null) {
                if (hit.typeOfHit == RayTraceResult.Type.BLOCK && this.getEntityWorld().getBlockState(hit.getBlockPos()).getBlock() == Blocks.PORTAL) {
                    this.setPortal(hit.getBlockPos());
                } else {
                    if (!ForgeHooks.onThrowableImpact(this, hit)) {
                        this.onImpact(hit);
                    }
                }
            }
        }
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
        for(this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {}
        while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }
        while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }
        while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }
        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    protected void calculateVelocityForOrbit() {
        double pX = this.posX;
        double pZ = this.posZ;
        double cX = this.getThrower().posX;
        double cZ = this.getThrower().posZ;

        double dx = pX - cX;
        double dz = pZ - cZ;
        double A = 0.5 * (RADIUS * RADIUS - VELOCITY * VELOCITY + pX * pX - cX * cX + pZ * pZ - cZ * cZ);
        double a = dx * dx + dz * dz;
        double b = -2 * (cX * dz * dz + A * dx - cZ * dx * dz);
        double c = A * A - dz * dz * (RADIUS * RADIUS - cX * cX - cZ * cZ) - 2 * cZ * dz * A;

        double D = b * b - 4 * a * c;

        if (D < 0) {
            this.setStage(EnumStage.PULLING);
            return;
        }

        double x1 = (-b + Math.sqrt(D)) / (2 * a);
        double z1 = (A - dx * x1) / dz;
        double x2 = (-b - Math.sqrt(D)) / (2 * a);
        double z2 = (A - dx * x2) / dz;

        double x;
        double y = this.getThrower().posY + this.getThrower().height / 2;
        double z;
        if (pZ >= cZ) {
            x = x1;
            z = z1;
        } else {
            x = x2;
            z = z2;
        }
        this.motionX = x - this.posX;
        this.motionY = y - this.posY;
        this.motionZ = z - this.posZ;
    }

    protected void setStage(EnumStage stage) {
        this.getDataManager().set(DATA_STAGE, stage);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        boolean shouldStop = false;
        if(result.typeOfHit == RayTraceResult.Type.ENTITY) {
            if(result.entityHit instanceof IProjectile) {
                if(result.entityHit instanceof EntityMagnetizedRock) {
                    EntityMagnetizedRock rock = (EntityMagnetizedRock) result.entityHit;
                    if(rock.getThrower() == this.getThrower()) {
                        return;
                    } else {
                        rock.setDead();
                        shouldStop = true;
                    }
                } else {
                    if (!getEntityWorld().isRemote) {
                        this.increaseHitCounter();
                    }
                    result.entityHit.setDead();
                    shouldStop = this.getHitsTaken() >= this.getPotencyEarth();
                }
            } else if(result.entityHit instanceof EntityLivingBase) {
                if(!this.getEntityWorld().isRemote && result.entityHit != this.getThrower()) {
                    EntityLivingBase entity = (EntityLivingBase) result.entityHit;
                    MagicDamageHandler.getInstance().dealDamage(entity, this.getPotencyEarth()*2, this, Element.EARTH, this.getPotencyEarth(), this.getDirection());
                }
                shouldStop = true;
            }
        } else if(result.typeOfHit == RayTraceResult.Type.BLOCK) {
            EnumStage stage = this.getStage();
            shouldStop = stage == EnumStage.THROWN || stage == EnumStage.ORBITING;
            if(shouldStop) {
                BlockPos pos = result.getBlockPos();
                pos = pos.offset(result.sideHit);
                this.setPosition(pos.getX(), pos.getY(), pos.getZ());
            }
        }
        if(shouldStop) {
            this.setDead();
        }
    }

    protected EnumStage getStage() {
        return this.getDataManager().get(DATA_STAGE);
    }

    protected int getHitsTaken() {
        return this.getDataManager().get(DATA_HITS);
    }

    protected void increaseHitCounter() {
        this.getDataManager().set(DATA_HITS, this.getHitsTaken() + 1);
    }

    @Override
    public void setDead() {
        if(!this.getEntityWorld().isRemote) {
            EntityFallingBlock fallingBlock = new EntityFallingBlock(this.getEntityWorld(), this.posX, this.posY, this.posZ, this.getBlockState());
            fallingBlock.fallTime = 1;
            this.getEntityWorld().spawnEntity(fallingBlock);
            if(this.effect != null) {
                this.effect.onEntityMagnetizedRockRemoved(this);
            }
        }
        super.setDead();
    }

    @Override
    protected NBTTagCompound writeDataToNBT(NBTTagCompound tag) {
        tag.setString(Names.NBT.CHARGE, Block.REGISTRY.getNameForObject(this.block.getBlock()).toString());
        tag.setInteger(Names.NBT.ELEMENT, this.block.getBlock().getMetaFromState(this.block));
        tag.setInteger(Names.NBT.EXPERIENCE, this.getStage().ordinal());
        tag.setInteger(Names.NBT.LEVEL, this.getHitsTaken());
        tag.setInteger(Names.NBT.EARTH, this.getPotencyEarth());
        return tag;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void readDataFromNBT(NBTTagCompound tag) {
        this.block = Block.getBlockFromName(tag.getString(Names.NBT.CHARGE)).getStateFromMeta(tag.getInteger(Names.NBT.ELEMENT));
        this.setStage(EnumStage.values()[tag.getInteger(Names.NBT.EXPERIENCE)]);
        this.getDataManager().set(DATA_HITS, tag.getInteger(Names.NBT.LEVEL));
        this.potencyEarth = tag.hasKey(Names.NBT.EARTH) ? tag.getInteger(Names.NBT.EARTH) : 0;
    }

    public enum EnumStage {
        PULLING,
        ORBITING,
        THROWN;

        public static DataSerializer<EnumStage> dataSerializer = new DataSerializer<EnumStage>() {
            @Override
            public void write(PacketBuffer buf, EnumStage value) {
                buf.writeInt(value.ordinal());
            }

            @Override
            public EnumStage read(PacketBuffer buf) {
                return values()[buf.readInt()];
            }

            @Override
            public DataParameter<EnumStage> createKey(int id) {
                return new DataParameter<>(id, this);
            }

            @Override
            public EnumStage copyValue(EnumStage value) {
                return value;
            }
        };
    }

    public static class RenderFactory implements IRenderFactory<EntityMagnetizedRock> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @SideOnly(Side.CLIENT)
        public Render<? super EntityMagnetizedRock> createRenderFor(RenderManager manager) {
            return new RenderEntityMagnetizedRock(manager);
        }
    }
}
