package com.teaminfinity.elementalinvocations.render.entity;

import com.teaminfinity.elementalinvocations.entity.EntityWaveForm;
import com.teaminfinity.elementalinvocations.reference.Reference;
import com.teaminfinity.elementalinvocations.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class RenderEntityWaveForm extends Render<EntityWaveForm> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/wave_form.png");

    public RenderEntityWaveForm(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityWaveForm e, double x, double y, double z, float entityYaw, float partialTicks) {
        EntityPlayer caster = e.getThrower();
        if(caster == null) {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.translate(x, y, z);

        new RenderUtil().renderCoordinateSystemDebug();

        Vec3d look = caster.getLook(partialTicks);

        double yaw = Math.atan2(look.xCoord, look.zCoord);

        GlStateManager.rotate((float) yaw, 0, 1, 0);

        new RenderUtil().renderCoordinateSystemDebug();

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWaveForm entity) {
        return TEXTURE;
    }
}
