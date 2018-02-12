package com.infinityraider.elementalinvocations.render.entity;

import com.infinityraider.elementalinvocations.entity.EntityMagicProjectile;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.infinitylib.reference.Constants;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

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
    protected void renderTexture(EntityMagicProjectile e, VertexBuffer buffer, Tessellator tessellator) {
        float u = Constants.UNIT;

        GlStateManager.color(e.getRed(), e.getGreen(), e.getBlue(), 127);

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        double minV = (getFrame()) * (1.0/FRAMES);
        double maxV = (getFrame() + 1) * (1.0/FRAMES);

        buffer.pos(-12 * u, 0, 0).tex(1, maxV).color(e.getRed(), e.getGreen(), e.getBlue(), 255).endVertex();
        buffer.pos(-12 * u, 24 * u, 0).tex(1, minV).color(e.getRed(), e.getGreen(), e.getBlue(), 255).endVertex();
        buffer.pos(12 * u, 24 * u, 0).tex(0, minV).color(e.getRed(), e.getGreen(), e.getBlue(), 255).endVertex();
        buffer.pos(12 * u, 0, 0).tex(0, maxV).color(e.getRed(), e.getGreen(), e.getBlue(), 255).endVertex();

        tessellator.draw();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMagicProjectile entity) {
        return TEXTURE;
    }
}
