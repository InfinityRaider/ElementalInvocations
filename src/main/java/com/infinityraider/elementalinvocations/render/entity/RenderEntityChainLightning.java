package com.infinityraider.elementalinvocations.render.entity;

import com.infinityraider.elementalinvocations.entity.EntityChainLightning;
import com.infinityraider.infinitylib.reference.Reference;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEntityChainLightning  extends Render<EntityChainLightning> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/lightning.png");

    public RenderEntityChainLightning(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityChainLightning e, double x, double y, double z, float entityYaw, float partialTicks) {

    }

    @Override
    protected ResourceLocation getEntityTexture(EntityChainLightning entity) {
        return TEXTURE;
    }
}