package com.teaminfinity.elementalinvocations.entity;

import com.infinityraider.infinitylib.network.NetworkWrapper;
import com.teaminfinity.elementalinvocations.ElementalInvocations;
import com.teaminfinity.elementalinvocations.handler.PlayerReplicaHandler;
import com.teaminfinity.elementalinvocations.magic.spell.death.EffectReplicate;
import com.teaminfinity.elementalinvocations.network.MessageSwapPlayerPosition;
import com.teaminfinity.elementalinvocations.reference.Names;
import com.teaminfinity.elementalinvocations.render.entity.RenderEntityReplicate;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class EntityReplicate extends EntityLiving implements IEntityAdditionalSpawnData {
    private EntityPlayer player;
    private int lifeTime;

    /** only exists on the server thread */
    private EffectReplicate spellEffect;

    public EntityReplicate(World world) {
        super(world);
    }

    public EntityReplicate(EntityPlayer player, EffectReplicate spellEffect, int lifeTime) {
        this(player.getEntityWorld());
        this.lifeTime = lifeTime;
        this.player = player;
        this.spellEffect = spellEffect;
        this.posX = player.posX;
        this.posY = player.posY;
        this.posZ = player.posZ;
        this.prevPosX = player.prevPosX;
        this.prevPosY = player.prevPosY;
        this.prevPosZ = player.prevPosZ;
        this.rotationYaw = player.rotationYaw;
        this.rotationPitch = player.rotationPitch;
        this.prevRotationYaw = player.prevRotationYaw;
        this.prevRotationPitch = player.prevRotationPitch;
        this.rotationYawHead = player.rotationYawHead;
        this.prevRotationYawHead = player.prevRotationYawHead;
        this.motionX = player.motionX;
        this.motionY = player.motionY;
        this.motionZ = player.motionZ;
        this.cameraPitch = player.prevCameraPitch;
        this.prevCameraPitch = player.prevCameraPitch;
        this.copyDataFromPlayer();
        PlayerReplicaHandler.getInstance().addReplica(this);
    }

    public void copyDataFromPlayer() {
        this.isSwingInProgress = this.getPlayer().isSwingInProgress;
        this.prevSwingProgress = this.getPlayer().prevSwingProgress;
        this.swingProgress = this.getPlayer().swingProgress;
        this.prevLimbSwingAmount = this.getPlayer().prevLimbSwingAmount;
        this.limbSwingAmount = this.getPlayer().limbSwingAmount;
        this.limbSwing = this.getPlayer().limbSwing;
    }

    public void swapWithPlayer() {
        if(this.isEntityAlive() && this.getPlayer() != null) {
            if(!this.getEntityWorld().isRemote) {
                NetworkWrapper.getInstance().sendToAll(new MessageSwapPlayerPosition(this));
            }
            //get current player position
            double playerX = this.getPlayer().posX;
            double playerY = this.getPlayer().posY;
            double playerZ = this.getPlayer().posZ;
            //set player's new position
            this.getPlayer().prevPosX = this.posX;
            this.getPlayer().prevPosY = this.posY;
            this.getPlayer().prevPosZ = this.posZ;
            this.getPlayer().setPosition(this.posX, this.posY, this.posZ);
            //set this position
            this.prevPosX = playerX;
            this.prevPosY = playerY;
            this.prevPosZ = playerZ;
            this.setPosition(playerX, playerY, playerZ);
        }
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    @SideOnly(Side.CLIENT)
    public AbstractClientPlayer getClientPlayer() {
        if(this.getPlayer() instanceof AbstractClientPlayer) {
            return (AbstractClientPlayer) this.getPlayer();
        }
        return (AbstractClientPlayer) ElementalInvocations.proxy.getClientPlayer();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.lifeTime--;
        if(this.getPlayer() == null || this.lifeTime < 0) {
            this.setDead();
        }
        if(!this.worldObj.isRemote && this.spellEffect == null) {
            this.setDead();
        }
    }

    @Override
    public void setDead() {
        if(this.spellEffect != null) {
            this.spellEffect.onReplicaDeath(this);
        }
        super.setDead();
    }

    @Override
    public void writeSpawnData(ByteBuf data) {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeEntityToNBT(tag);
        ByteBufUtils.writeTag(data, tag);
    }

    @Override
    public void readSpawnData(ByteBuf data) {
        NBTTagCompound tag = ByteBufUtils.readTag(data);
        this.readEntityFromNBT(tag);
    }

    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        if(this.getPlayer() != null) {
            tag.setString(Names.NBT.PLAYER, this.getPlayer().getUniqueID().toString());
        }
        tag.setInteger(Names.NBT.COUNT, this.lifeTime);
    }

    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        if(tag.hasKey(Names.NBT.PLAYER)) {
            this.player = this.getEntityWorld().getPlayerEntityByUUID(UUID.fromString(tag.getString(Names.NBT.PLAYER)));
        }
        this.lifeTime = tag.getInteger(Names.NBT.COUNT);
    }

    public static class RenderFactory implements IRenderFactory<EntityReplicate> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static final RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        public Render<? super EntityReplicate> createRenderFor(RenderManager manager) {
            return new RenderEntityReplicate(manager);
        }
    }
}
