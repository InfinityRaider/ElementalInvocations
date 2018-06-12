package com.infinityraider.elementalinvocations.render.entity;

import com.infinityraider.elementalinvocations.entity.EntityStaticRemnant;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderEntityStaticRemnant extends Render<EntityStaticRemnant> {
    private static final ModelPlayer MODEL_MALE = new ModelPlayer(0.0F, false);
    private static final ModelPlayer MODEL_FEMALE = new ModelPlayer(0.0F, true);

    public RenderEntityStaticRemnant(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void doRender(EntityStaticRemnant entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();

        GlStateManager.translate(x, y ,z);

        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);
        GlStateManager.color(0.0F, 1.0F, 0.0F, 1.0F);

        this.bindEntityTexture(entity);
        this.getMainModel(entity).render(entity, 0, 0, 0, 0, 0, 1);

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }


    public ModelPlayer getMainModel(EntityStaticRemnant entity) {
        return DefaultPlayerSkin.getSkinType(entity.getCasterUniqueId()).equals("slim") ? MODEL_FEMALE : MODEL_MALE;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityStaticRemnant entity) {
        AbstractClientPlayer player = entity.getClientPlayer();
        return player == null ? DefaultPlayerSkin.getDefaultSkin(entity.getCasterUniqueId()) : player.getLocationSkin();
    }
}