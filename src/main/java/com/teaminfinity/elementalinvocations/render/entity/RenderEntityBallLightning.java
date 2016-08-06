package com.teaminfinity.elementalinvocations.render.entity;

import com.teaminfinity.elementalinvocations.ElementalInvocations;
import com.teaminfinity.elementalinvocations.entity.EntityBallLightning;
import com.teaminfinity.elementalinvocations.reference.Constants;
import com.teaminfinity.elementalinvocations.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
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

    private float lastPartialTick = -1;
    private int counter = 0;
    private int frame;

    public RenderEntityBallLightning(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityBallLightning e, double x, double y, double z, float entityYaw, float partialTicks) {
        List<Entity> mounted = e.getPassengers();
        if(mounted.size() > 0 && mounted.get(0) == ElementalInvocations.proxy.getClientPlayer() && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
            lastPartialTick = -1;
            counter = 0;
            frame = 0;
            //don't render the entity in first person
            return;
        }
        calculateFrame(partialTicks);
        super.doRender(e, x, y, z, entityYaw, partialTicks);
    }

    private void calculateFrame(float partialTicks) {
        if(lastPartialTick < 0) {
            frame = 0;
        } else {
            if(partialTicks <= lastPartialTick) {
                counter = (counter + 1) % FRAME_TIME;
                frame = counter == 0 ? (frame + 1) % FRAMES : frame;
            }
        }
        lastPartialTick = partialTicks;
    }

    @Override
    protected void renderTexture(EntityBallLightning entity, VertexBuffer buffer, Tessellator tessellator) {
        float u = Constants.UNIT;

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);

        GlStateManager.color(255, 255, 255, 127);

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        double minV = (frame) * (1.0/FRAMES);
        double maxV = (frame + 1) * (1.0/FRAMES);

        buffer.pos(-12 * u, 0, 0).tex(1, maxV).endVertex();
        buffer.pos(-12 * u, 24 * u, 0).tex(1, minV).endVertex();
        buffer.pos(12 * u, 24 * u, 0).tex(0, minV).endVertex();
        buffer.pos(12 * u, 0, 0).tex(0, maxV).endVertex();

        tessellator.draw();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityBallLightning entity) {
        return TEXTURE;
    }
}
