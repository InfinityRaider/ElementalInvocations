package com.teaminfinity.elementalinvocations.render.entity;

import com.teaminfinity.elementalinvocations.entity.EntityMagicProjectile;
import com.teaminfinity.elementalinvocations.reference.Constants;
import com.teaminfinity.elementalinvocations.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderEntityMagicProjectile extends Render<EntityMagicProjectile> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/magic_missile.png");

    public RenderEntityMagicProjectile(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityMagicProjectile e, double x, double y, double z, float entityYaw, float partialTicks) {
        Minecraft.getMinecraft().renderEngine.bindTexture(getEntityTexture(e));
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.translate(x, y, z);

        //rotate so the texture always renders parallel to the screen
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.rotate(-renderManager.playerViewY, 0, 1, 0);
        GlStateManager.rotate(renderManager.playerViewX, 1, 0, 0);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(e));
        }

        float u = Constants.UNIT;

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        buffer.pos(-8 * u, 0, 0).tex(1, 1).color(e.getRed(), e.getGreen(), e.getBlue(), 255).endVertex();
        buffer.pos(-8 * u, 16 * u, 0).tex(1, 0).color(e.getRed(), e.getGreen(), e.getBlue(), 255).endVertex();
        buffer.pos(8 * u, 16 * u, 0).tex(0, 0).color(e.getRed(), e.getGreen(), e.getBlue(), 255).endVertex();
        buffer.pos(8 * u, 0, 0).tex(0, 1).color(e.getRed(), e.getGreen(), e.getBlue(), 255).endVertex();

        tessellator.draw();

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();

    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMagicProjectile entity) {
        return TEXTURE;
    }
}
