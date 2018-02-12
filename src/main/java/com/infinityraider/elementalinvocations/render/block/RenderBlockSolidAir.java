package com.infinityraider.elementalinvocations.render.block;

import com.google.common.collect.ImmutableList;
import com.infinityraider.elementalinvocations.block.BlockSolidAir;
import com.infinityraider.elementalinvocations.block.tile.TileSolidAir;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.infinitylib.render.block.RenderBlockWithTileBase;
import com.infinityraider.infinitylib.render.tessellation.ITessellator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.List;

public class RenderBlockSolidAir extends RenderBlockWithTileBase<BlockSolidAir, TileSolidAir> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "blocks\\solid_air");

    public RenderBlockSolidAir(BlockSolidAir block) {
        super(block, new TileSolidAir(), false, true, false);
    }

    @Override
    public void renderWorldBlockDynamic(ITessellator tessellator, World world, BlockPos pos, double x, double y, double z,
                                        BlockSolidAir block, TileSolidAir tile, float partialTick, int destroyStage) {}

    @Override
    public void renderInventoryBlock(ITessellator tessellator, World world, IBlockState state, BlockSolidAir block, TileSolidAir tile,
                                     ItemStack stack, EntityLivingBase entity, ItemCameraTransforms.TransformType type) {}

    @Override
    public List<ResourceLocation> getAllTextures() {
        return ImmutableList.of(TEXTURE);
    }

    @Override
    public void renderWorldBlockStatic(ITessellator tessellator, IBlockState state, BlockSolidAir block, EnumFacing side) {
        if(side != null) {
            if (state instanceof IExtendedBlockState) {
                if (!((IExtendedBlockState) state).getValue(BlockSolidAir.PROPERTY_VISIBLE)) {
                    tessellator.drawScaledFace(0, 0, 16, 16, side, getIcon(), side.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? 16 : 0);
                }
            }
        }
    }

    @Override
    public TextureAtlasSprite getIcon() {
        return getIcon(TEXTURE);
    }

    @Override
    public boolean applyAmbientOcclusion() {
        return false;
    }
}