package com.infinityraider.elementalinvocations.block;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.magic.MagicDamageHandler;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.elementalinvocations.render.block.RenderBlockImpaleSpike;
import com.infinityraider.infinitylib.block.BlockCustomRenderedBase;
import com.infinityraider.infinitylib.block.blockstate.InfinityProperty;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockImpaleSpike extends BlockCustomRenderedBase {
    public static final AxisAlignedBB BOX = new AxisAlignedBB(0.0625D, 0, 0.0625D, 0.9375D, 1.5, 0.9375D);

    public BlockImpaleSpike() {
        super("ei.block.impale_spike", Material.ROCK);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.STONE);
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        if(entity instanceof EntityLivingBase) {
            MagicDamageHandler.getInstance().dealDamage(entity, 3, Element.EARTH, 1);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if(entity instanceof EntityLivingBase) {
            MagicDamageHandler.getInstance().dealDamage(entity, 3, Element.EARTH, 1);
        }
    }

    @Override
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Blocks.STONE.getItemDropped(Blocks.STONE.getDefaultState(), rand, fortune);
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return BOX;
    }


    @Override
    @Deprecated
    @Nullable
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return BOX;
    }

    @Override
    @Deprecated
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
        return BOX.offset(pos);
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RenderBlockImpaleSpike getRenderer() {
        return new RenderBlockImpaleSpike(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getBlockModelResourceLocation() {
        return new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + getInternalName());
    }

    @Override
    protected InfinityProperty[] getPropertyArray() {
        return new InfinityProperty[] {};
    }
}