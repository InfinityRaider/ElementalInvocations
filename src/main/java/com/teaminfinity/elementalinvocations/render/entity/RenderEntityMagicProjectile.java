package com.teaminfinity.elementalinvocations.render.entity;

import com.teaminfinity.elementalinvocations.entity.EntityMagicProjectile;
import com.teaminfinity.elementalinvocations.reference.Reference;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEntityMagicProjectile extends Render<EntityMagicProjectile> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/magic_missile.png");

    public RenderEntityMagicProjectile(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMagicProjectile entity) {
        return TEXTURE;
    }
}
