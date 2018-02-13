package com.infinityraider.elementalinvocations.block;


import com.infinityraider.elementalinvocations.block.tile.TileSolidAir;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.elementalinvocations.render.block.RenderBlockSolidAir;
import com.infinityraider.infinitylib.block.*;
import com.infinityraider.infinitylib.block.blockstate.InfinityProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class BlockSolidAir extends BlockTileCustomRenderedBase<TileSolidAir> {
    public static final PropertyVisible PROPERTY_VISIBLE = new PropertyVisible();
    public static final AxisAlignedBB EMPTY_BB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

    public BlockSolidAir() {
        super("ei.block.solid_air", Material.BARRIER);
        this.setBlockUnbreakable();
        this.setResistance(6000000.0F);
    }

    @Override
    public TileSolidAir createNewTileEntity(World world, int meta) {
        return new TileSolidAir();
    }

    public Optional<TileSolidAir> getTile(IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof TileSolidAir ? Optional.of((TileSolidAir) tile) : Optional.empty();
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        Optional<TileSolidAir> tile = getTile(world, pos);
        if (tile.isPresent()) {
            return ((IExtendedBlockState) state).withProperty(PROPERTY_VISIBLE, tile.get().isVisible());
        } else {
            return state;
        }
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
        entity.fall(fallDistance, 0.0F);
    }

    @Override
    public void onLanded(World world, Entity entity) {
        if (entity.motionY < 0.0D) {
            entity.motionY = -0.3*entity.motionY;
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        //TODO: render effect and play sound
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, net.minecraft.client.particle.ParticleManager manager) {
        //TODO: render effect and play sound
        return false;
    }

    @Override
    @Deprecated
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return BlockSolidAir.EMPTY_BB;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getBlockModelResourceLocation() {
        return new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + getInternalName());
    }

    @Override
    protected InfinityProperty[] getPropertyArray() {
        return new InfinityProperty[]{};
    }

    @Override
    public IUnlistedProperty[] getUnlistedPropertyArray() {
        return new IUnlistedProperty[]{PROPERTY_VISIBLE};
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RenderBlockSolidAir getRenderer() {
        return new RenderBlockSolidAir(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        IBlockState stateAt = world.getBlockState(pos.offset(side));
        return stateAt.getBlock() != this && super.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return true;
    }

    public static class PropertyVisible implements IUnlistedProperty<Boolean> {
        private PropertyVisible() {
        }

        @Override
        public String getName() {
            return "visible";
        }

        @Override
        public boolean isValid(Boolean value) {
            return true;
        }

        @Override
        public Class<Boolean> getType() {
            return Boolean.class;
        }

        @Override
        public String valueToString(Boolean value) {
            return value.toString();
        }
    }
}