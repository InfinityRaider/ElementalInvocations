package com.infinityraider.elementalinvocations.render.entity;

import com.infinityraider.elementalinvocations.entity.EntityMagnetizedRock;
import com.infinityraider.infinitylib.render.tessellation.TessellatorVertexBuffer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class RenderEntityMagnetizedRock extends Render<EntityMagnetizedRock> {
    public RenderEntityMagnetizedRock(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void doRender(EntityMagnetizedRock entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.translate(x, y, z);

        IBlockState state = entity.getBlockState();
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);

        this.bindEntityTexture(entity);

        for(EnumFacing facing : EnumFacing.values()) {
            List<BakedQuad> quads = model.getQuads(state, facing, 0);
            if (quads.size() > 0) {
                TessellatorVertexBuffer tessellator = TessellatorVertexBuffer.getInstance();
                tessellator.startDrawingQuads(quads.get(0).getFormat());
                tessellator.addQuads(quads);
                tessellator.draw();
            }
        }

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMagnetizedRock entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
