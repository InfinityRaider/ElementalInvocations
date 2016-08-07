package com.teaminfinity.elementalinvocations.render.entity;

import com.teaminfinity.elementalinvocations.entity.EntityVacuum;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEntityVacuum extends Render<EntityVacuum> {
    public RenderEntityVacuum(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityVacuum entity, double x, double y, double z, float entityYaw, float partialTicks) {}

    @Override
    protected ResourceLocation getEntityTexture(EntityVacuum entity) {
        return null;
    }
}
