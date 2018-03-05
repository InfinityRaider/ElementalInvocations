package com.infinityraider.elementalinvocations.render.block;

import com.infinityraider.elementalinvocations.block.BlockEarthquake;
import com.infinityraider.elementalinvocations.block.tile.TileEarthquake;
import com.infinityraider.infinitylib.render.block.RenderBlockWithTileBase;
import com.infinityraider.infinitylib.render.tessellation.ITessellator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class RenderBlockEarthquake extends RenderBlockWithTileBase<BlockEarthquake, TileEarthquake> {
    public RenderBlockEarthquake(BlockEarthquake block) {
        super(block, new TileEarthquake(), false, true, true);
    }

    @Override
    public void renderWorldBlockDynamic(ITessellator tessellator, World world, BlockPos pos, double x, double y, double z,
                                        BlockEarthquake block, TileEarthquake tile, float partialTick, int destroyStage) {

        IBlockState state = tile.getOriginalState();
        if(state == null) {
            return;
        }
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
        if(model == null) {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        float frame = (tile.getFrame() + partialTick);
        double dy = 0.25*Math.cos(2*Math.PI*frame/tile.getMaxFrames());
        GlStateManager.scale(1, 1+dy, 1);

        //ugly, need to fix this in InfinityLib
        tessellator.draw();
        for(EnumFacing facing : EnumFacing.values()) {
            List<BakedQuad> quads = model.getQuads(state, facing, 0);
            if (quads.size() > 0) {
                tessellator.startDrawingQuads(quads.get(0).getFormat());
                tessellator.addQuads(quads);
                tessellator.draw();
            }
        }
        tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    @Override
    public void renderInventoryBlock(ITessellator tessellator, World world, IBlockState state, BlockEarthquake block, TileEarthquake tile,
                                     ItemStack stack, EntityLivingBase entity, ItemCameraTransforms.TransformType type) {

    }

    @Override
    public List<ResourceLocation> getAllTextures() {
        return Collections.emptyList();
    }

    @Override
    public void renderWorldBlockStatic(ITessellator tessellator, IBlockState state, BlockEarthquake block, EnumFacing side) {

    }

    @Override
    public TextureAtlasSprite getIcon() {
        return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
    }

    @Override
    public boolean applyAmbientOcclusion() {
        return true;
    }
}