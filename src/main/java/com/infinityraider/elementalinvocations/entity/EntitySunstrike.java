package com.infinityraider.elementalinvocations.entity;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.magic.MagicDamageHandler;
import com.infinityraider.elementalinvocations.reference.Names;
import com.infinityraider.elementalinvocations.registry.SoundRegistry;
import com.infinityraider.infinitylib.utility.AreaHelper;
import com.infinityraider.elementalinvocations.network.MessageRenderSunstrike;
import com.infinityraider.elementalinvocations.render.entity.RenderEntitySunstrike;
import com.infinityraider.infinitylib.sound.ModSoundHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class EntitySunstrike extends Entity implements IEntityAdditionalSpawnData {
    private int timer;
    private int potencyFire;
    private int potencyAir;

    @SideOnly(Side.CLIENT)
    private boolean shouldRender;

    public EntitySunstrike(World world) {
        super(world);
        this.timer = 60;
    }

    public EntitySunstrike(World world, double x, double z, int potencyFire, int potencyAir) {
        this(world);
        this.posX = x;
        this.posZ = z;
        this.posY = getYForXZ(x, z);
        this.timer = 60;
        this.potencyFire = potencyFire;
        this.potencyAir = potencyAir;
    }

    private double getYForXZ(double x, double z) {
        int min = 0;
        BlockPos pos = new BlockPos(x, 255, z);
        IBlockState state = getEntityWorld().getBlockState(pos);
        while (state.getBlock().isAir(state, getEntityWorld(), pos) && pos.getY() > min) {
            pos = pos.down();
            state = getEntityWorld().getBlockState(pos);
        }
        return pos.getY() + 1;
    }

    public int getPotencyFire() {
        return this.potencyFire;
    }

    public int getPotencyAir() {
        return this.potencyAir;
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        timer = timer - 1;
        if (!getEntityWorld().isRemote) {
            if (timer == 0) {
                new MessageRenderSunstrike(this).sendToAll();
                AxisAlignedBB area = AreaHelper.getArea(this.getPositionVector(), Math.max(1, getPotencyAir() / 3));
                List<EntityLivingBase> entities = getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, area);
                entities.forEach(e ->
                                MagicDamageHandler.getInstance().dealDamage(e, getPotencyFire() * 2, Element.FIRE, this.getPotencyFire(), new Vec3d(0, 1, 0))
                );
                ModSoundHandler.getInstance().playSoundAtEntityOnce(this, SoundRegistry.getInstance().SOUND_SUNSTRIKE, SoundCategory.PLAYERS);
            }
            if (timer <= -10) {
                this.setDead();
            }
        } else {
            if(timer == 0) {
                this.setShouldRender();
            }
        }
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.PLAYERS;
    }

    @Override
    protected void entityInit() {}

    @SideOnly(Side.CLIENT)
    public void setShouldRender() {
        this.shouldRender = true;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldRender() {
        return shouldRender;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.potencyFire = compound.getInteger(Names.NBT.LEVEL);
        this.potencyAir = compound.getInteger(Names.NBT.COUNT);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger(Names.NBT.LEVEL, this.potencyFire);
        compound.setInteger(Names.NBT.COUNT, this.potencyAir);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(this.potencyFire);
        buffer.writeInt(this.potencyAir);
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        this.potencyFire = buffer.readInt();
        this.potencyAir = buffer.readInt();
    }


    public static class RenderFactory implements IRenderFactory<EntitySunstrike> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @SideOnly(Side.CLIENT)
        public Render<? super EntitySunstrike> createRenderFor(RenderManager manager) {
            return new RenderEntitySunstrike(manager);
        }
    }
}
