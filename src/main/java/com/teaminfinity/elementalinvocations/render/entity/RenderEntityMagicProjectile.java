package com.teaminfinity.elementalinvocations.render.entity;

import com.teaminfinity.elementalinvocations.entity.EntityMagicProjectile;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEntityMagicProjectile extends Render<EntityMagicProjectile> {
    public RenderEntityMagicProjectile(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMagicProjectile entity) {
        return null;
    }
}
