package com.infinityraider.elementalinvocations.render.entity;

import com.infinityraider.elementalinvocations.entity.EntityChainLightning;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.infinitylib.render.tessellation.ITessellator;
import com.infinityraider.infinitylib.render.tessellation.TessellatorVertexBuffer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderEntityChainLightning  extends  RenderEntityAnimated<EntityChainLightning> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/lightning.png");

    private static final int FRAMES = 4;
    private static final int FRAME_TIME = 4;

    public RenderEntityChainLightning(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityChainLightning entity, double x, double y, double z, float entityYaw, float partialTicks) {
        EntityLivingBase source = entity.getSource();
        EntityLivingBase target = entity.getTarget();
        if(source == null || target == null) {
            return;
        }

        double x0 = entity.prevPosX + partialTicks*(entity.posX - entity.prevPosX);
        double y0 = entity.prevPosY + partialTicks*(entity.posY - entity.prevPosY);
        double z0 = entity.prevPosZ + partialTicks*(entity.posZ - entity.prevPosZ);

        float x1 = (float) (target.prevPosX + partialTicks*(target.posX - target.prevPosX) - x0);
        float y1 = (float) (target.prevPosY + partialTicks*(target.posY - target.prevPosY) - y0) + target.getEyeHeight()/2;
        float z1 = (float) (target.prevPosZ + partialTicks*(target.posZ - target.prevPosZ) - z0);

        float x2 = (float) (source.prevPosX + partialTicks*(source.posX - source.prevPosX) - x0);
        float y2 = (float) (source.prevPosY + partialTicks*(source.posY - source.prevPosY) - y0) + target.getEyeHeight()/2;
        float z2 = (float) (source.prevPosZ + partialTicks*(source.posZ - source.prevPosZ) - z0);

        ITessellator tessellator = TessellatorVertexBuffer.getInstance();
        calculateFrame(partialTicks, FRAMES, FRAME_TIME);

        float minV = (getFrame()) * (1.0F/FRAMES);
        float maxV = (getFrame() + 1) * (1.0F/FRAMES);
        float dY = 0.5F;

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.translate(x, y, z);

        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);

        tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.bindTexture(this.getEntityTexture(entity));
        tessellator.setColorRGBA(1, 1, 1, 0.5F);

        tessellator.addVertexWithUV(x1 - dY, y1 - dY, z1 - dY, 0, maxV);
        tessellator.addVertexWithUV(x1 + dY, y1 + dY, z1 + dY, 0, minV);
        tessellator.addVertexWithUV(x2 + dY, y2 + dY, z2 + dY, 1, minV);
        tessellator.addVertexWithUV(x2 - dY, y2 - dY, z2 - dY, 1, maxV);

        tessellator.addVertexWithUV(x1 - dY, y1 - dY, z1 - dY, 0, maxV);
        tessellator.addVertexWithUV(x2 - dY, y2 - dY, z2 - dY, 1, maxV);
        tessellator.addVertexWithUV(x2 + dY, y2 + dY, z2 + dY, 1, minV);
        tessellator.addVertexWithUV(x1 + dY, y1 + dY, z1 + dY, 0, minV);

        tessellator.addVertexWithUV(x1 + dY, y1 - dY, z1 + dY, 0, maxV);
        tessellator.addVertexWithUV(x1 - dY, y1 + dY, z1 - dY, 0, minV);
        tessellator.addVertexWithUV(x2 - dY, y2 + dY, z2 - dY, 1, minV);
        tessellator.addVertexWithUV(x2 + dY, y2 - dY, z2 + dY, 1, maxV);

        tessellator.addVertexWithUV(x1 + dY, y1 - dY, z1 + dY, 0, maxV);
        tessellator.addVertexWithUV(x2 + dY, y2 - dY, z2 + dY, 1, maxV);
        tessellator.addVertexWithUV(x2 - dY, y2 + dY, z2 - dY, 1, minV);
        tessellator.addVertexWithUV(x1 - dY, y1 + dY, z1 - dY, 0, minV);

        tessellator.draw();

        Tessellator tess = Tessellator.getInstance();
        VertexBuffer buff = tess.getBuffer();
        buff.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        buff.pos(x1, y1, z1).color(1.F, 0.F, 0.F, 1.F).endVertex();
        buff.pos(x2, y2, z2).color(1.F, 0.F, 0.F, 1.F).endVertex();
        tess.draw();

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();

    }

    @Override
    protected ResourceLocation getEntityTexture(EntityChainLightning entity) {
        return TEXTURE;
    }
}