package com.infinityraider.elementalinvocations.render.block;

import com.infinityraider.elementalinvocations.block.BlockImpaleSpike;
import com.infinityraider.infinitylib.render.block.RenderBlockBase;
import com.infinityraider.infinitylib.render.tessellation.ITessellator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class RenderBlockImpaleSpike extends RenderBlockBase<BlockImpaleSpike> {
    public RenderBlockImpaleSpike(BlockImpaleSpike block) {
        super(block, false);
    }

    @Override
    public List<ResourceLocation> getAllTextures() {
        return Collections.emptyList();
    }

    @Override
    public void renderWorldBlockStatic(ITessellator tessellator, IBlockState state, BlockImpaleSpike block, EnumFacing side) {
        //Primary spike
        this.drawSpike(tessellator, 24, 12, 2, 2, 8, 8);

        //Secondary spike 1
        this.drawSpike(tessellator, 16, 6, 2, 6, 0, 12);

        //Secondary spike 2
        this.drawSpike(tessellator, 12, 8, 5, 12, 2, 14);

        //Secondary spike 3
        this.drawSpike(tessellator, 14, 7, 4, 8, 8, 1);

        //Secondary spike 4
        this.drawSpike(tessellator, 18, 8, 6, 4, 15, 8);
    }

    protected void drawSpike(ITessellator tessellator, int height, int width, int offX, int offZ, int tipX, int tipZ) {
        //Face 1
        tessellator.addScaledVertexWithUV(offX, 0, offZ, this.getIcon(), 0, 16);
        tessellator.addScaledVertexWithUV(tipX, height, tipZ, this.getIcon(), 0, 0);
        tessellator.addScaledVertexWithUV(tipX, height, tipZ, this.getIcon(), 16, 0);
        tessellator.addScaledVertexWithUV(offX + width, 0, offZ, this.getIcon(), 16, 16);
        //Face 2
        tessellator.addScaledVertexWithUV(offX + width, 0, offZ, this.getIcon(), 0, 16);
        tessellator.addScaledVertexWithUV(tipX, height, tipZ, this.getIcon(), 0, 0);
        tessellator.addScaledVertexWithUV(tipX, height, tipZ, this.getIcon(), 16, 0);
        tessellator.addScaledVertexWithUV(offX + width, 0, offZ + width, this.getIcon(), 16, 16);
        //Face 3
        tessellator.addScaledVertexWithUV(offX + width, 0, offZ + width, this.getIcon(), 0, 16);
        tessellator.addScaledVertexWithUV(tipX, height, tipZ, this.getIcon(), 0, 0);
        tessellator.addScaledVertexWithUV(tipX, height, tipZ, this.getIcon(), 16, 0);
        tessellator.addScaledVertexWithUV(offX, 0, offZ + width, this.getIcon(), 16, 16);
        //Face 4
        tessellator.addScaledVertexWithUV(offX, 0, offZ + width, this.getIcon(), 0, 16);
        tessellator.addScaledVertexWithUV(tipX, height, tipZ, this.getIcon(), 0, 0);
        tessellator.addScaledVertexWithUV(tipX, height, tipZ, this.getIcon(), 16, 0);
        tessellator.addScaledVertexWithUV(offX, 0, offZ, this.getIcon(), 16, 16);
    }

    @Override
    public void renderInventoryBlock(ITessellator tessellator, World world, IBlockState state, BlockImpaleSpike block,
                                     ItemStack stack, EntityLivingBase entity, ItemCameraTransforms.TransformType type) {
    }

    @Override
    public TextureAtlasSprite getIcon() {
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("stone");
    }

    @Override
    public boolean applyAmbientOcclusion() {
        return false;
    }
}