package com.teaminfinity.elementalinvocations.entity;

import com.infinityraider.infinitylib.network.NetworkWrapper;
import com.teaminfinity.elementalinvocations.ElementalInvocations;
import com.teaminfinity.elementalinvocations.entity.ai.EntityMoveHelperTest;
import com.teaminfinity.elementalinvocations.magic.spell.death.EffectReplicate;
import com.teaminfinity.elementalinvocations.network.MessageSwapPlayerPosition;
import com.teaminfinity.elementalinvocations.network.MessageTrackPlayer;
import com.teaminfinity.elementalinvocations.reference.Names;
import com.teaminfinity.elementalinvocations.render.entity.RenderEntityReplicate;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

public class EntityReplicate extends EntityLiving implements IEntityAdditionalSpawnData {
    private static Map<UUID, List<EntityReplicate>> replicaMap = new HashMap<>();

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
        this.addToReplicaMap();
        this.moveHelper = new EntityMoveHelperTest(this);
    }

    protected void addToReplicaMap() {
        if(!this.getEntityWorld().isRemote && this.getPlayer() instanceof EntityPlayerMP) {
            if (!replicaMap.containsKey(this.getPlayer().getUniqueID())) {
                replicaMap.put(this.getPlayer().getUniqueID(), new ArrayList<>());
            }
            List<EntityReplicate> replicas = replicaMap.get(this.getPlayer().getUniqueID());
            if (replicas.isEmpty()) {
                NetworkWrapper.getInstance().sendTo(new MessageTrackPlayer(true), (EntityPlayerMP) this.getPlayer());
            }
            replicas.add(this);
        }
    }

    protected void removeFromReplicaMap() {
        if(!this.getEntityWorld().isRemote && this.getPlayer() instanceof EntityPlayerMP) {
            if (!replicaMap.containsKey(this.getPlayer().getUniqueID())) {
                //impossible, but hey, better safe than sorry
                replicaMap.put(this.getPlayer().getUniqueID(), new ArrayList<>());
            }
            List<EntityReplicate> replicas = replicaMap.get(this.getPlayer().getUniqueID());
            replicas.remove(this);
            if (replicas.isEmpty()) {
                NetworkWrapper.getInstance().sendTo(new MessageTrackPlayer(false), (EntityPlayerMP) this.getPlayer());
            }
        }
    }

    protected void initEntityAI() {}

    public EntityPlayer getPlayer() {
        return player;
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

    public void copyPlayerMovement(double deltaX, double deltaY, double deltaZ) {
        if(this.getPlayer() == null || this.getEntityWorld().isRemote) {
            return;
        }
        if(deltaX == 0 && deltaY == 0 && deltaZ == 0) {
            return;
        }
        this.motionX = deltaX;
        this.motionY = deltaY;
        this.motionZ = deltaZ;
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
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    @Override
    public void setDead() {
        if(this.spellEffect != null) {
            this.spellEffect.onReplicaDeath(this);
            this.removeFromReplicaMap();
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


    /**
     * Method overrides forwarded to the player
     */

    @Override
    @Nullable
    public ItemStack getHeldItemMainhand() {
        if(this.getPlayer() == null) {
            return null;
        }
        ItemStack stack = this.getPlayer().getHeldItemMainhand().copy();
        return stack == null ? null : stack.copy();
    }

    @Override
    @Nullable
    public ItemStack getHeldItemOffhand() {
        if(this.getPlayer() == null) {
            return null;
        }
        ItemStack stack = this.getPlayer().getHeldItemOffhand().copy();
        return stack == null ? null : stack.copy();
    }

    @Override
    public void setHeldItem(EnumHand hand, @Nullable ItemStack stack) {
        //Nope
    }

    @Override
    public ItemStack getHeldItem(EnumHand hand) {
        if(this.getPlayer() == null) {
            return null;
        }
        ItemStack stack = this.getPlayer().getHeldItem(hand).copy();
        return stack == null ? null : stack.copy();
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slot) {
        if(this.getPlayer() == null) {
            return null;
        }
        ItemStack stack = this.getPlayer().getItemStackFromSlot(slot);
        return stack == null ? null : stack.copy();
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slot, ItemStack stack) {
        //Nope
    }

    @Override
    public Iterable<ItemStack> getHeldEquipment() {
        if(this.getPlayer() == null) {
            return new IterableCopyStack();
        }
        return new IterableCopyStack(this.getPlayer().getHeldEquipment());
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        if(this.getPlayer() == null) {
            return new IterableCopyStack();
        }
        return new IterableCopyStack(this.getPlayer().getArmorInventoryList());
    }

    @Override
    public Iterable<ItemStack> getEquipmentAndArmor() {
        if(this.getPlayer() == null) {
            return new IterableCopyStack();
        }
        return new IterableCopyStack(this.getPlayer().getEquipmentAndArmor());
    }

    @Override
    public void damageArmor(float damage) {
        //Nope
    }

    @Override
    public void damageShield(float damage) {
        //Nope
    }

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source) {
        //Nope
    }

    @Override
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
        //Nope
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        //Nope
    }

    @Override
    public boolean replaceItemInInventory(int slot, @Nullable ItemStack stack) {
        //Nope
        return false;
    }

    public static void updatePlayerMovement(EntityPlayer player, double deltaX, double deltaY, double deltaZ) {
        if(!player.getEntityWorld().isRemote && replicaMap.containsKey(player.getUniqueID())) {
            List<EntityReplicate> replicas = replicaMap.get(player.getUniqueID());
            for(EntityReplicate replica : replicas) {
                replica.copyPlayerMovement(deltaX, deltaY, deltaZ);
            }
        }
    }

    public static class RenderFactory implements IRenderFactory<EntityReplicate> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @SideOnly(Side.CLIENT)
        public Render<? super EntityReplicate> createRenderFor(RenderManager manager) {
            return new RenderEntityReplicate(manager);
        }
    }



    //No, don't scroll below here, there's nothing to see down there









    private static class IterableCopyStack implements Iterable<ItemStack> {
        private Iterable<ItemStack> parent;

        private IterableCopyStack() {
            this(null);
        }

        private IterableCopyStack(Iterable<ItemStack> parent) {
            this.parent = parent;
        }

        @Override
        public Iterator<ItemStack> iterator() {
            return new IteratorCopyStack(this.parent == null ? null : this.parent.iterator());
        }
    }

    private static class IteratorCopyStack implements Iterator<ItemStack> {
        private Iterator<ItemStack> parent;

        private IteratorCopyStack(Iterator<ItemStack> parent) {
            this.parent = parent;
        }

        @Override
        public boolean hasNext() {
            return this.parent != null && this.parent.hasNext();
        }

        @Override
        public ItemStack next() {
            if(this.parent == null) {
                //shouldn't ever happen
                return null;
            }
            ItemStack stack = parent.next();
            return stack == null ? null : stack.copy();
        }
    }
}
