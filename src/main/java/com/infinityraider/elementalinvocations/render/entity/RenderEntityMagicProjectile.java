package com.infinityraider.elementalinvocations.render.entity;

import com.infinityraider.elementalinvocations.entity.EntityMagicProjectile;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.infinitylib.render.tessellation.ITessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntityMagicProjectile extends RenderEntityFlatTexture<EntityMagicProjectile> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/magic_missile.png");

    public RenderEntityMagicProjectile(RenderManager renderManager) {
        super(renderManager);
    }
    private static final int FRAMES = 12;
    private static final int FRAME_TIME = 1;

    @Override
    public void doRender(EntityMagicProjectile e, double x, double y, double z, float entityYaw, float partialTicks) {
        calculateFrame(partialTicks, FRAME_TIME, FRAMES);
        super.doRender(e, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void renderTexture(EntityMagicProjectile e, ITessellator tessellator) {
        GlStateManager.color(e.getRed(), e.getGreen(), e.getBlue(), 0.5F);

        float minV = 16*(getFrame()) * (1.0F/FRAMES);
        float maxV = 16*(getFrame() + 1) * (1.0F/FRAMES);

        tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);

        tessellator.setColorRGBA(e.getRed(), e.getGreen(), e.getBlue(), 0.5F);

        tessellator.addScaledVertexWithUV(-12, 0, 0, 16, maxV);
        tessellator.addScaledVertexWithUV(-12, 24, 0, 16, minV);
        tessellator.addScaledVertexWithUV(12, 24, 0, 0, minV);
        tessellator.addScaledVertexWithUV(12, 0, 0, 0, maxV);

        tessellator.draw();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMagicProjectile entity) {
        return TEXTURE;
    }
}
