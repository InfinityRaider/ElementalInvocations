package com.teaminfinity.elementalinvocations.render.entity;

import com.teaminfinity.elementalinvocations.entity.EntityWaveForm;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEntityWaveForm extends Render<EntityWaveForm> {
    public RenderEntityWaveForm(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWaveForm entity) {
        return null;
    }
}
