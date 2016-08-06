package com.teaminfinity.elementalinvocations.entity;

import com.google.common.collect.ImmutableList;
import com.teaminfinity.elementalinvocations.api.Element;
import com.teaminfinity.elementalinvocations.api.IMagicCharge;
import com.teaminfinity.elementalinvocations.magic.generic.MagicEffect;
import com.teaminfinity.elementalinvocations.reference.Names;
import com.teaminfinity.elementalinvocations.render.entity.RenderEntityMagicProjectile;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class EntityMagicProjectile extends EntityThrowableMagic {
    private static final AxisAlignedBB BOX = new AxisAlignedBB(0, 0, 0, 0.25, 0.25, 0.25);

    private List<IMagicCharge> charges;
    private int timer;
    private int red = -1;
    private int green = -1;
    private int blue = -1;

    @SuppressWarnings("unused")
    public EntityMagicProjectile(World world) {
        super(world);
        this.setEntityBoundingBox(BOX);
    }

    public EntityMagicProjectile(EntityPlayer caster, List<IMagicCharge> charges) {
        super(caster);
        this.charges = ImmutableList.copyOf(charges);
        this.setEntityBoundingBox(BOX);
        this.setThrowableHeading(getDirection().xCoord, getDirection().yCoord, getDirection().zCoord, 2F, 0.5F);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!this.getEntityWorld().isRemote) {
            timer = timer + 1;
            if(timer > 5000) {
                this.setDead();
            }
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if(!worldObj.isRemote) {
            if(result.entityHit != null && (result.entityHit instanceof EntityLivingBase)) {
                new MagicEffect(getThrower(), (EntityLivingBase) result.entityHit, getDirection(), charges).apply();
            }
        }
        this.setDead();
    }

    @Override
    protected float getGravityVelocity() {
        return 0.05F;
    }

    public int getRed() {
        if(red < 0) {
            compileColors();
        }
        return red;
    }

    public int getGreen() {
        if(green < 0) {
            compileColors();
        }
        return green;
    }

    public int getBlue() {
        if(blue < 0) {
            compileColors();
        }
        return blue;
    }

    private void compileColors() {
        int total = 0;
        this.red = 0;
        this.green = 0;
        this.blue = 0;
        for(IMagicCharge charge : charges) {
            total = total + charge.level();
            this.red = this.red + charge.element().getRed()*charge.level();
            this.green = this.green + charge.element().getGreen()*charge.level();
            this.blue = this.blue + charge.element().getBlue()*charge.level();
        }
        this.red = this.red / total;
        this.green = this.green / total;
        this.blue = this.blue / total;
    }

    @Override
    protected NBTTagCompound writeDataToNBT(NBTTagCompound tag) {
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

    @Override
    protected void readDataFromNBT(NBTTagCompound tag) {
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
