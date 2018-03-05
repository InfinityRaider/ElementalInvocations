package com.infinityraider.elementalinvocations.block;

import com.infinityraider.elementalinvocations.block.tile.TileEarthquake;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.elementalinvocations.render.block.RenderBlockEarthquake;
import com.infinityraider.infinitylib.block.BlockTileCustomRenderedBase;
import com.infinityraider.infinitylib.block.blockstate.InfinityProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEarthquake extends BlockTileCustomRenderedBase<TileEarthquake> {
    public static final InfinityProperty[] PROPERTIES = new InfinityProperty[] {};

    public BlockEarthquake() {
        super("ei.block.earthquake", Material.ROCK);
    }

    @Override
    public TileEarthquake createNewTileEntity(World world, int meta) {
        return new TileEarthquake();
    }

    @Override
    protected InfinityProperty[] getPropertyArray() {
        return PROPERTIES;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public RenderBlockEarthquake getRenderer() {
        return new RenderBlockEarthquake(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelResourceLocation getBlockModelResourceLocation() {
        return new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + getInternalName());
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}