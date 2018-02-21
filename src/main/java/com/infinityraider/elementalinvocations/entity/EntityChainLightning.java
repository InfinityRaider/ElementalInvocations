package com.infinityraider.elementalinvocations.entity;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.magic.MagicDamageHandler;
import com.infinityraider.elementalinvocations.magic.spell.air.EffectChainLightning;
import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.elementalinvocations.render.entity.RenderEntityChainLightning;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Optional;
import java.util.UUID;

public class EntityChainLightning extends Entity implements IEntityAdditionalSpawnData {
    private static final int RANGE = 5;

    private EntityPlayer caster;
    private EntityLivingBase target;
    private EntityChainLightning parent;
    private EntityChainLightning daughter;

    private UUID casterId;
    private int targetId;
    private int parentId;
    private int daughterId;

    private int potency;
    private int index;
    private boolean master;

    public EntityChainLightning(World world) {
        super(world);
    }

    public EntityChainLightning(EntityPlayer caster, EntityLivingBase target, int potency) {
        this(caster.getEntityWorld());
        this.caster = caster;
        this.casterId = caster.getUniqueID();
        this.target = target;
        this.targetId = target.getEntityId();
        this.updatePosition();
        this.index = potency;
        this.potency = potency;
        this.master = true;
    }

    public EntityChainLightning(EntityChainLightning parent, EntityLivingBase target, int index) {
        this(parent.getCaster(), target, parent.getPotency());
        this.parent = parent;
        this.parentId = parent.getEntityId();
        this.index = index;
        this.master = false;
    }

    public boolean isMaster() {
        return this.master;
    }

    public EntityPlayer getCaster() {
        if(this.caster == null) {
            if(this.casterId != null) {
                this.caster = this.getEntityWorld().getPlayerEntityByUUID(this.casterId);
            }
        }
        return this.caster;
    }

    public UUID getCasterId() {
        return this.casterId;
    }

    public EntityLivingBase getTarget() {
        if(this.target == null) {
            if(this.targetId >= 0) {
                Entity target = this.getEntityWorld().getEntityByID(this.targetId);
                if(target instanceof EntityLivingBase) {
                    this.target = (EntityLivingBase) target;
                }
            }
        }
        return this.target;
    }

    public int getPotency() {
        return this.potency;
    }

    public Optional<EntityChainLightning> getDaughter() {
        if(this.daughter == null) {
            if(this.daughterId >= 0) {
                Entity daughter = this.getEntityWorld().getEntityByID(this.daughterId);
                if(daughter instanceof EntityChainLightning) {
                    this.daughter = (EntityChainLightning) daughter;
                }
            }
        }
        return Optional.ofNullable(this.daughter);
    }

    public Optional<EntityChainLightning> getParent() {
        if(this.parent == null) {
            if(this.parentId >= 0) {
                Entity parent = this.getEntityWorld().getEntityByID(this.parentId);
                if(parent instanceof EntityChainLightning) {
                    this.parent = (EntityChainLightning) parent;
                }
            }
        }
        return Optional.ofNullable(this.parent);
    }

    public EntityLivingBase getSource() {
        return this.isMaster() ? this.getCaster() : this.getParent().map(EntityChainLightning::getTarget).orElse(null);
    }

    public boolean isTarget(EntityLivingBase other) {
        return other != null && (this.getTarget() == other || this.isParentTarget(other) || this.isDaughterTarget(other));
    }

    protected boolean isParentTarget(EntityLivingBase target) {
        return this.getParent().map(parent -> parent.getTarget() == target || parent.isParentTarget(target)).orElse(false);
    }

    protected boolean isDaughterTarget(EntityLivingBase target) {
        return this.getDaughter().map(daughter -> daughter.getTarget() == target || daughter.isDaughterTarget(target)).orElse(false);
    }

    protected void searchAndSetNextTarget() {
        if(this.index <= 0) {
            return;
        }
        this.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, this.getSearchBox(), e -> e != this.getCaster() && !this.isTarget(e)).stream().findAny().ifPresent(target -> {
            this.daughter = new EntityChainLightning(this, target, this.index - 1);
            this.getEntityWorld().spawnEntityInWorld(this.daughter);
            this.daughterId = this.daughter.getEntityId();
        });
    }

    protected AxisAlignedBB getSearchBox() {
        return new AxisAlignedBB(this.posX - RANGE, this.posY - RANGE, this.posZ - RANGE, this.posX + RANGE, this.posY + RANGE, this.posZ + RANGE);
    }

    protected boolean distanceCheck() {
        Optional<? extends Entity> check;
        if(this.isMaster()) {
            check = Optional.ofNullable(this.getCaster());
        } else {
            check = this.getParent();
        }
        return check.map(source -> source.getDistanceSqToEntity(this.getTarget()) <= RANGE*RANGE).orElse(false);
    }

    protected void damageTarget() {
        if(this.getTarget() == null) {
            return;
        }
        if(this.getTarget() instanceof EntityCreeper) {
            if(!((EntityCreeper) this.getTarget()).getPowered()) {
                this.getTarget().onStruckByLightning(null); //OK for vanilla creepers, potentially dangerous with modded creepers, we'll see...
            }
        } else {
            MagicDamageHandler.getInstance().dealDamage(this.getTarget(), this.getPotency()/2.0F, this.getCaster(), Element.AIR, this.getPotency());
        }
    }

    protected void updatePosition() {
        if(this.getTarget() != null) {
            EntityLivingBase target = this.getTarget();
            this.posX = target.posX;
            this.posY = target.posY;
            this.posZ = target.posZ;
        }
    }

    @Override
    public void onEntityUpdate() {
        this.updatePosition();
        if(!this.getEntityWorld().isRemote) {
            if(this.getCaster() == null || !this.getCaster().isEntityAlive()) {
                this.setDead();
                return;
            }
            if(this.getTarget() == null || !this.getTarget().isEntityAlive()) {
                this.setDead();
                return;
            }
            if (!this.distanceCheck()) {
                this.setDead();
                return;
            }
            this.damageTarget();
            if(!this.getDaughter().isPresent()) {
                this.searchAndSetNextTarget();
            }
        }
    }

    @Override
    public void setDead() {
        this.getDaughter().ifPresent(EntityChainLightning::setDead);
        this.getParent().ifPresent(parent -> {
            parent.daughter = null;
            parent.daughterId = -1;
        });
        if(!this.getEntityWorld().isRemote) {
            EffectChainLightning.onChainStopped(this);
        }
        super.setDead();
    }

    @Override
    protected void entityInit() {}

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        //caster
        String uuid = tag.hasKey(Names.NBT.PLAYER) ? tag.getString(Names.NBT.PLAYER) : "null";
        if(uuid.equalsIgnoreCase("null")) {
            this.casterId = null;
        } else {
            this.casterId = UUID.fromString(uuid);
        }
        //target
        this.targetId = tag.hasKey(Names.NBT.TARGET) ? tag.getInteger(Names.NBT.TARGET) : -1;
        //parent
        this.parentId = tag.hasKey(Names.NBT.PARENT) ? tag.getInteger(Names.NBT.PARENT) : -1;
        //daughter
        this.daughterId = tag.hasKey(Names.NBT.DAUGHTER) ? tag.getInteger(Names.NBT.DAUGHTER) : -1;
        //others
        this.potency = tag.hasKey(Names.NBT.AIR) ? tag.getInteger(Names.NBT.AIR) : 0;
        this.index = tag.hasKey(Names.NBT.COUNT) ? tag.getInteger(Names.NBT.COUNT) : 0;
        this.master = tag.hasKey(Names.NBT.CHARGE) && tag.getBoolean(Names.NBT.CHARGE);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
        //caster
        tag.setString(Names.NBT.PLAYER, this.casterId.toString());
        //target
        tag.setInteger(Names.NBT.TARGET, this.targetId);
        //parent
        tag.setInteger(Names.NBT.PARENT, this.parentId);
        //daughter
        tag.setInteger(Names.NBT.DAUGHTER, this.daughterId);
        //others
        tag.setInteger(Names.NBT.AIR, this.potency);
        tag.setInteger(Names.NBT.COUNT, this.index);
        tag.setBoolean(Names.NBT.CHARGE, this.master);
    }

    @Override
    public final void writeSpawnData(ByteBuf buffer) {
        ByteBufUtils.writeTag(buffer, this.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public final void readSpawnData(ByteBuf buffer) {
        this.readFromNBT(ByteBufUtils.readTag(buffer));
    }

    public static class RenderFactory implements IRenderFactory<EntityChainLightning> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @SideOnly(Side.CLIENT)
        public Render<? super EntityChainLightning> createRenderFor(RenderManager manager) {
            return new RenderEntityChainLightning(manager);
        }
    }
}