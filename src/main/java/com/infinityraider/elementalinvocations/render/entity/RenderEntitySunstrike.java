package com.infinityraider.elementalinvocations.render.entity;

import com.infinityraider.elementalinvocations.entity.EntitySunstrike;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.infinitylib.render.tessellation.ITessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntitySunstrike extends RenderEntityFlatTexture<EntitySunstrike> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/sunstrike.png");

    public RenderEntitySunstrike(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected void renderTexture(EntitySunstrike entity, ITessellator tessellator) {
        if(entity.shouldRender()) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.disableLighting();
            GlStateManager.color(255, 255, 255, 127);

            tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX_COLOR);
            tessellator.setColorRGBA(1, 1, 1, 0.5F);
            tessellator.drawScaledCylinder(0, 0, 0, 24, 500, 16, 16);
            tessellator.draw();
        }
    }

    @Override
    protected boolean rotateX() {
        return false;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySunstrike entity) {
        return TEXTURE;
    }
}
