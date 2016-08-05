package com.teaminfinity.elementalinvocations.render.entity;

import com.teaminfinity.elementalinvocations.entity.EntityBallLightning;
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
public class RenderEntityBallLightning extends RenderEntityFlatTexture<EntityBallLightning> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/ball_lightning.png");

    public RenderEntityBallLightning(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected void renderTexture(EntityBallLightning entity, VertexBuffer buffer, Tessellator tessellator) {
        float u = Constants.UNIT;

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        buffer.pos(-8 * u, 0, 0).tex(1, 1).endVertex();
        buffer.pos(-8 * u, 16 * u, 0).tex(1, 0).endVertex();
        buffer.pos(8 * u, 16 * u, 0).tex(0, 0).endVertex();
        buffer.pos(8 * u, 0, 0).tex(0, 1).endVertex();

        tessellator.draw();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityBallLightning entity) {
        return TEXTURE;
    }
}
