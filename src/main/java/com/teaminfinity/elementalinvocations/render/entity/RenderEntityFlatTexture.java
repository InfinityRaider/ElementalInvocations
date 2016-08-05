package com.teaminfinity.elementalinvocations.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

public abstract class RenderEntityFlatTexture<E extends Entity> extends Render<E> {
    protected RenderEntityFlatTexture(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(E e, double x, double y, double z, float entityYaw, float partialTicks) {
        Minecraft.getMinecraft().renderEngine.bindTexture(getEntityTexture(e));
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.translate(x, y, z);

        //rotate so the texture always renders parallel to the screen
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        if(rotateY()) {
            GlStateManager.rotate(-renderManager.playerViewY, 0, 1, 0);
        }
        if(rotateX()) {
            GlStateManager.rotate(renderManager.playerViewX, 1, 0, 0);
        }

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(e));
        }

        this.renderTexture(e, buffer, tessellator);

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    protected abstract void renderTexture(E entity, VertexBuffer buffer, Tessellator tessellator);

    protected boolean rotateY() {
        return true;
    }

    protected boolean rotateX() {
        return true;
    }
}
