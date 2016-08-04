package com.teaminfinity.elementalinvocations.entity;

import com.google.common.collect.ImmutableList;
import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.IMagicCharge;
import com.teaminfinity.elementalinvocations.reference.Names;
import com.teaminfinity.elementalinvocations.render.entity.RenderEntityMagicProjectile;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EntityMagicProjectile extends EntityThrowable implements IEntityAdditionalSpawnData {
    private List<IMagicCharge> charges;

    @SuppressWarnings("unused")
    public EntityMagicProjectile(World world) {
        super(world);
    }

    public EntityMagicProjectile(EntityPlayer caster, List<IMagicCharge> charges) {
        super(caster.getEntityWorld(), caster);
        this.charges = ImmutableList.copyOf(charges);
    }

    @Override
    protected void onImpact(RayTraceResult result) {

    }

    @Override
    protected float getGravityVelocity() {
        return 0.03F;
    }

    @Override
    @Nullable
    public EntityPlayer getThrower() {
        return (EntityPlayer) super.getThrower();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        this.writeChargeListToNBT(tag);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.readChargeListFromNBT(tag);
    }

    private NBTTagCompound writeChargeListToNBT(NBTTagCompound tag) {
        NBTTagList list = new NBTTagList();
        for(IMagicCharge charge : charges) {
            NBTTagCompound chargeTag = new NBTTagCompound();
            chargeTag.setInteger(Names.NBT.LEVEL, charge.level());
            chargeTag.setInteger(Names.NBT.ELEMENT, charge.element().ordinal());
            list.appendTag(chargeTag);
        }
        tag.setTag(Names.NBT.CHARGE, list);
        return tag;
    }

    private void readChargeListFromNBT(NBTTagCompound tag) {
        ArrayList<IMagicCharge> list = new ArrayList<>();
        NBTTagList tagList = tag.getTagList(Names.NBT.CHARGE, 10);
        for(int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound chargeTag = tagList.getCompoundTagAt(i);
            Element element = Element.values()[chargeTag.getInteger(Names.NBT.ELEMENT)];
            int level = chargeTag.getInteger(Names.NBT.LEVEL);
            list.add(new IMagicCharge() {
                @Override
                public Element element() {
                    return element;
                }

                @Override
                public int level() {
                    return level;
                }
            });
        }
        this.charges = ImmutableList.copyOf(list);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        ByteBufUtils.writeTag(buffer, this.writeChargeListToNBT(new NBTTagCompound()));
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        this.readChargeListFromNBT(ByteBufUtils.readTag(buffer));
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
