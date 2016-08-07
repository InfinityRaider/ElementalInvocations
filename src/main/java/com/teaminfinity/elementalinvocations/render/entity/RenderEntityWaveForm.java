package com.teaminfinity.elementalinvocations.render.entity;

import com.teaminfinity.elementalinvocations.entity.EntityWaveForm;
import com.teaminfinity.elementalinvocations.reference.Constants;
import com.teaminfinity.elementalinvocations.reference.Reference;
import com.teaminfinity.elementalinvocations.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class RenderEntityWaveForm extends Render<EntityWaveForm> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/wave_form.png");

    public RenderEntityWaveForm(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityWaveForm e, double x, double y, double z, float entityYaw, float partialTicks) {
        Minecraft.getMinecraft().renderEngine.bindTexture(getEntityTexture(e));
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

        new RenderUtil().renderCoordinateSystemDebug();
        float u = Constants.UNIT;

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        //left front
        buffer.pos(0, 0, 0).tex(1, 1).endVertex();
        buffer.pos(0, 16 * u, 0).tex(1, 0).endVertex();
        buffer.pos(-4 * u, 16 * u, 16 * u).tex(0, 0).endVertex();
        buffer.pos(-4 * u, 0, 16 * u).tex(0, 1).endVertex();

        //left back
        buffer.pos(0, 0, 0).tex(1, 1).endVertex();
        buffer.pos(-4 * u, 0, 16 * u).tex(0, 1).endVertex();
        buffer.pos(-4 * u, 16 * u, 16 * u).tex(0, 0).endVertex();
        buffer.pos(0, 16 * u, 0).tex(1, 0).endVertex();

        //right front
        buffer.pos(0, 0, 0).tex(1, 1).endVertex();
        buffer.pos(0, 16 * u, 0).tex(1, 0).endVertex();
        buffer.pos(-4 * u, 16 * u, 16 * u).tex(0, 0).endVertex();
        buffer.pos(-4 * u, 0, 16 * u).tex(0, 1).endVertex();

        //right back
        buffer.pos(0, 0, 0).tex(1, 1).endVertex();
        buffer.pos(-4 * u, 0, -16 * u).tex(0, 1).endVertex();
        buffer.pos(-4 * u, 16 * u, -16 * u).tex(0, 0).endVertex();
        buffer.pos(0, 16 * u, 0).tex(1, 0).endVertex();

        tessellator.draw();

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWaveForm entity) {
        return TEXTURE;
    }
}
