package com.infinityraider.elementalinvocations.block;

import com.infinityraider.elementalinvocations.block.tile.TileEarthquake;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.elementalinvocations.render.block.RenderBlockEarthquake;
import com.infinityraider.infinitylib.block.BlockTileCustomRenderedBase;
import com.infinityraider.infinitylib.block.blockstate.InfinityProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public Optional<TileEarthquake> getTile(IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        return tile instanceof TileEarthquake ? Optional.of((TileEarthquake) tile) : Optional.empty();
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        this.getTile(world, pos).ifPresent(tile -> tile.affectEntity(entity));
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        this.onEntityWalk(world, pos, entity);
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return getTile(world, pos).map(TileEarthquake::getOriginalState)
                .orElse(state);
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return getTile(world, pos).map(tile -> tile.getOriginalState().getBlockHardness(world, pos))
                .orElse(super.getBlockHardness(state, world, pos));
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return getTile(world, pos).map(TileEarthquake::getBoundingBox).orElse(FULL_BLOCK_AABB);
    }

    @Nullable
    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        return getBoundingBox(state, world, pos);
    }

    @Override
    @Deprecated
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
        return getBoundingBox(state, world, pos);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return this.getTile(world, pos).map(tile -> tile.getOriginalBlock().getDrops(world, pos, tile.getOriginalState(), fortune))
                .orElse(Collections.emptyList());
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return this.getTile(world, pos).map(tile -> tile.getOriginalBlock().canHarvestBlock(world, pos, player))
                .orElse(false);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return this.getTile(world, pos).map(tile -> tile.getOriginalBlock().removedByPlayer(tile.getOriginalState(), world, pos, player, willHarvest))
                .orElse(super.removedByPlayer(state, world, pos, player, willHarvest));
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return this.getTile(world, pos).map(tile -> tile.getOriginalBlock().canSilkHarvest(world, pos, tile.getOriginalState(), player))
                .orElse(false);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        return this.getTile(world, pos).map(tile -> tile.getOriginalBlock().getExplosionResistance(exploder))
                .orElse(super.getExplosionResistance(world, pos, exploder, explosion));
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return this.getTile(world, pos).map(tile -> tile.getOriginalBlock().canConnectRedstone(tile.getOriginalState(), world, pos, side))
                .orElse(false);
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return this.getTile(world, pos).map(tile -> tile.getOriginalBlock().getPickBlock(tile.getOriginalState(), target, world, pos, player))
                .orElse(null);
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return this.getTile(world, pos).map(tile -> tile.getOriginalBlock().canEntityDestroy(tile.getOriginalState(), world, pos, entity))
                .orElse(super.canEntityDestroy(state, world, pos, entity));
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        return this.getTile(world, pos).map(tile -> tile.getOriginalBlock().getExpDrop(tile.getOriginalState(), world, pos, fortune))
                .orElse(0);
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