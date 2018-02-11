package com.infinityraider.elementalinvocations.entity;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.magic.MagicDamageHandler;
import com.infinityraider.elementalinvocations.magic.PotencyMap;
import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.elementalinvocations.render.entity.RenderEntityMagicProjectile;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityMagicProjectile extends EntityThrowableMagic {
    private static final AxisAlignedBB BOX = new AxisAlignedBB(0, 0, 0, 0.25, 0.25, 0.25);

    private IPotencyMap potencies;
    private int timer;

    @SuppressWarnings("unused")
    public EntityMagicProjectile(World world) {
        super(world);
        this.setEntityBoundingBox(BOX);
        this.potencies = new PotencyMap();
    }

    public EntityMagicProjectile(EntityPlayer caster, IPotencyMap potencies) {
        super(caster);
        this.potencies = potencies;
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
            if(result.entityHit != null) {
                for(Element element : Element.values()) {
                    int potency = this.potencies.getPotency(element);
                    if(potency > 0) {
                        MagicDamageHandler.getInstance().dealDamage(result.entityHit, potency, this, element, potency, this.getDirection());
                    }
                }
            }
        }
        this.setDead();
    }

    @Override
    protected float getGravityVelocity() {
        return 0.05F;
    }

    public float getRed() {
        return this.potencies.getRed();
    }

    public float getGreen() {
        return this.potencies.getGreen();
    }

    public float getBlue() {
        return this.potencies.getBlue();
    }

    @Override
    protected NBTTagCompound writeDataToNBT(NBTTagCompound tag) {
        tag.setTag(Names.NBT.CHARGE, this.potencies.writeToNBT());
        return tag;
    }

    @Override
    protected void readDataFromNBT(NBTTagCompound tag) {
        this.potencies.readFromNBT(tag.getCompoundTag(Names.NBT.CHARGE));
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
