package com.teaminfinity.elementalinvocations.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

public abstract class RenderEntityFlatTexture<E extends Entity> extends Render<E> {
    private float lastPartialTick = -1;
    private int counter = 0;
    private int frame;

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
            boolean invert = Minecraft.getMinecraft().gameSettings.thirdPersonView == 2;
            GlStateManager.rotate((invert ? -1 : 1) * renderManager.playerViewX, 1, 0, 0);
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

    protected void calculateFrame(float partialTicks, int frameTime, int frames) {
        if(lastPartialTick < 0) {
            frame = 0;
            counter = 0;
        } else {
            if(partialTicks <= lastPartialTick) {
                counter = (counter + 1) % frameTime;
                frame = counter == 0 ? (frame + 1) % frames : frame;
            }
        }
        lastPartialTick = partialTicks;
    }

    protected int getFrame() {
        return frame;
    }

    protected abstract void renderTexture(E entity, VertexBuffer buffer, Tessellator tessellator);

    protected boolean rotateY() {
        return true;
    }

    protected boolean rotateX() {
        return true;
    }
}
