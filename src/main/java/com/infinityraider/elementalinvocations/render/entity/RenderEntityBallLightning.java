package com.infinityraider.elementalinvocations.render.entity;

import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.entity.EntityBallLightning;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.infinitylib.render.tessellation.ITessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class RenderEntityBallLightning extends RenderEntityFlatTexture<EntityBallLightning> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/ball_lightning.png");

    private static final int FRAMES = 7;
    private static final int FRAME_TIME = 2;

    public RenderEntityBallLightning(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityBallLightning e, double x, double y, double z, float entityYaw, float partialTicks) {
        List<Entity> mounted = e.getPassengers();
        if(mounted.size() > 0 && mounted.get(0) == ElementalInvocations.proxy.getClientPlayer() && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
            //don't render the entity in first person
            return;
        }
        calculateFrame(partialTicks, FRAME_TIME, FRAMES);
        super.doRender(e, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void renderTexture(EntityBallLightning entity, ITessellator tessellator) {
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);

        GlStateManager.color(255, 255, 255, 127);

        float minV = 16*(getFrame()) * (1.0F/FRAMES);
        float maxV = 16*(getFrame() + 1) * (1.0F/FRAMES);

        tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);

        tessellator.setColorRGBA(1, 1, 1, 0.5F);

        tessellator.addScaledVertexWithUV(-12, 0, 0, 16, maxV);
        tessellator.addScaledVertexWithUV(-12, 24 , 0, 16, minV);
        tessellator.addScaledVertexWithUV(12, 24, 0, 0, minV);
        tessellator.addScaledVertexWithUV(12, 0, 0, 0, maxV);

        tessellator.draw();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityBallLightning entity) {
        return TEXTURE;
    }
}
