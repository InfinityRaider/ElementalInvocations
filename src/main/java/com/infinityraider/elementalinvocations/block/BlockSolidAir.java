package com.infinityraider.elementalinvocations.block;


import com.infinityraider.elementalinvocations.block.tile.TileSolidAir;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.elementalinvocations.render.block.RenderBlockSolidAir;
import com.infinityraider.infinitylib.block.*;
import com.infinityraider.infinitylib.block.blockstate.InfinityProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Optional;

public class BlockSolidAir extends BlockTileCustomRenderedBase<TileSolidAir> {
    public static final PropertyVisible PROPERTY_VISIBLE = new PropertyVisible();

    public BlockSolidAir() {
        super("ei.block.solid_air", Material.BARRIER);
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
        return BlockRenderLayer.CUTOUT_MIPPED;
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
        return super.shouldSideBeRendered(state, world, pos, side);
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

    @Override
    @SideOnly(value = Side.CLIENT)
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) {
        return false;
    }

    @Override
    @SideOnly(value = Side.CLIENT)
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager) {
        return false;
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