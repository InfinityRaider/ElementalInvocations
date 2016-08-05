package com.teaminfinity.elementalinvocations.render.entity;

import com.teaminfinity.elementalinvocations.entity.EntityMeteor;
import com.teaminfinity.elementalinvocations.reference.Reference;
import com.teaminfinity.elementalinvocations.render.model.ModelMeteor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEntityMeteor extends Render<EntityMeteor> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/meteor.png");
    private static final ModelMeteor MODEL = new ModelMeteor();

    public RenderEntityMeteor(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityMeteor e, double x, double y, double z, float entityYaw, float partialTicks) {
        Minecraft.getMinecraft().renderEngine.bindTexture(getEntityTexture(e));

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.translate(x, y, z);

        float newAngle = (float) (360 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

        MODEL.render(e, 0, 0, 0, 0, 0, 0);

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMeteor entity) {
        return TEXTURE;
    }
}
