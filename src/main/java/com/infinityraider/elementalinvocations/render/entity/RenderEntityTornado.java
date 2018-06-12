package com.infinityraider.elementalinvocations.render.entity;

import com.infinityraider.elementalinvocations.entity.EntityTornado;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.infinitylib.render.tessellation.TessellatorVertexBuffer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderEntityTornado extends Render<EntityTornado> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/tornado.png");

    public RenderEntityTornado(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityTornado e, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        int period = 20;
        int layers = 8;

        GlStateManager.translate(x, y, z);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);
        GlStateManager.color(1, 1, 1, 0.5F);

        for(int i = 0; i < layers; i ++) {
            drawTornadoLayer(period, i, layers);
        }

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    protected void drawTornadoLayer(int period, int layer, int total) {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        boolean even = layer % 2 == 0;
        int r = 8*(layer + 1);
        int time = (int) (System.currentTimeMillis() % (period*r));
        float angle = (360.0F*time)/(period*r);
        int dy = 16;
        int y = layer*dy;
        int height = Integer.min(2, total - layer)*dy;
        int vMax = (16*height)/dy;
        int quads = 16;

        GlStateManager.rotate((even ? 1 : -1)*angle, 0, 1, 0);
        GlStateManager.rotate((total - layer)/2.0F, (layer % 2 == 0 ? 1 : -1), 0, 0);

        TessellatorVertexBuffer tessellator = TessellatorVertexBuffer.getInstance();
        tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
        tessellator.bindTexture(TEXTURE);
        tessellator.setColorRGBA(1, 1, 1, 0.5F);

        tessellator.drawScaledCylinder(0, y, 0, r, height, vMax, quads);

        tessellator.draw();

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTornado entity) {
        return TEXTURE;
    }
}