package com.teaminfinity.elementalinvocations.render.entity;

import com.teaminfinity.elementalinvocations.entity.EntitySunstrike;
import com.teaminfinity.elementalinvocations.reference.Constants;
import com.teaminfinity.elementalinvocations.reference.Reference;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEntitySunstrike extends RenderEntityFlatTexture<EntitySunstrike> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/magic_missile.png");

    public RenderEntitySunstrike(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected void renderTexture(EntitySunstrike entity, VertexBuffer buffer, Tessellator tessellator) {
        if(entity.shouldRender()) {
            float u = Constants.UNIT;

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

            buffer.pos(-8 * 3 * u, 0, 0).tex(1, 1).color(255, 255, 255, 127).endVertex();
            buffer.pos(-8 * 3 * u, 500, 0).tex(1, 0).color(255, 255, 255, 127).endVertex();
            buffer.pos(8 * 3 * u, 500, 0).tex(0, 0).color(255, 255, 255, 127).endVertex();
            buffer.pos(8 * 3 * u, 0, 0).tex(0, 1).color(255, 255, 255, 127).endVertex();

            tessellator.draw();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySunstrike entity) {
        return TEXTURE;
    }
}
