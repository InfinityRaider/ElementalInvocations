package com.teaminfinity.elementalinvocations.render.entity;

import com.teaminfinity.elementalinvocations.entity.EntityWaveForm;
import com.teaminfinity.elementalinvocations.reference.Constants;
import com.teaminfinity.elementalinvocations.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class RenderEntityWaveForm extends RenderEntityAnimated<EntityWaveForm> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/wave_form.png");
    private static final ResourceLocation TEXTURE_BACK = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/wave_back.png");

    private static final int FRAMES = 4;
    private static final int FRAME_TIME = 2;

    private static final int BLURS = 5;

    public RenderEntityWaveForm(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityWaveForm e, double x, double y, double z, float entityYaw, float partialTicks) {
        calculateFrame(partialTicks, FRAME_TIME, FRAMES);

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.translate(x, y, z);

        //get caster
        EntityPlayer caster = e.getThrower();
        if(caster == null) {
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
            return;
        }

        //rotate in the direction of the caster' view
        Vec3d look = caster.getLook(partialTicks);
        double yaw = Math.toDegrees(Math.atan2(look.zCoord, look.xCoord));
        GlStateManager.rotate((float) -yaw, 0, 1, 0);

        //gl settings
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);

        float u = Constants.UNIT;
        int width = 20;

        double minV = (getFrame()) * (1.0/FRAMES);
        double maxV = (getFrame() + 1) * (1.0/FRAMES);


        for(int blur = 0; blur < BLURS; blur++) {
            GlStateManager.color(255, 255, 255, 255 - (150*blur/BLURS));
            float scale = 1.0F - (blur * 0.15F);


            //render front
            Minecraft.getMinecraft().renderEngine.bindTexture(getEntityTexture(e));
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

            //left
            buffer.pos(0, 0, 0).tex(1, maxV).endVertex();
            buffer.pos(0, 16 * u * scale, 0).tex(1, minV).endVertex();
            buffer.pos(-4 * u * scale, 16 * u * scale, width * u * scale).tex(0, minV).endVertex();
            buffer.pos(-4 * u * scale, 0, width * u * scale).tex(0, maxV).endVertex();

            //right
            buffer.pos(0, 0, 0).tex(1, maxV).endVertex();
            buffer.pos(-4 * u * scale, 0, -width * u * scale).tex(0, maxV).endVertex();
            buffer.pos(-4 * u * scale, 16 * u * scale, -width * u * scale).tex(0, minV).endVertex();
            buffer.pos(0, 16 * u * scale, 0).tex(1, minV).endVertex();

            tessellator.draw();


            //render back
            Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE_BACK);
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

            //left
            buffer.pos(0, 0, 0).tex(1, maxV).endVertex();
            buffer.pos(-4 * u * scale, 0, width * u * scale).tex(0, maxV).endVertex();
            buffer.pos(-4 * u * scale, 16 * u * scale, width * u * scale).tex(0, minV).endVertex();
            buffer.pos(0, 16 * u * scale, 0).tex(1, minV).endVertex();

            //right
            buffer.pos(0, 0, 0).tex(1, maxV).endVertex();
            buffer.pos(0, 16 * u * scale, 0).tex(1, minV).endVertex();
            buffer.pos(-4 * u * scale, 16 * u * scale, -width * u * scale).tex(0, minV).endVertex();
            buffer.pos(-4 * u * scale, 0, -width * u * scale).tex(0, maxV).endVertex();

            tessellator.draw();


            GlStateManager.translate(0.2, 0, 0);
        }

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWaveForm entity) {
        return TEXTURE;
    }
}
