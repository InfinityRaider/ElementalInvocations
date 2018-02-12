package com.infinityraider.elementalinvocations.render.entity;

import com.infinityraider.elementalinvocations.entity.EntityWaveForm;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.infinitylib.render.tessellation.ITessellator;
import com.infinityraider.infinitylib.render.tessellation.TessellatorVertexBuffer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEntityWaveForm extends RenderEntityAnimated<EntityWaveForm> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/wave_form.png");
    private static final ResourceLocation TEXTURE_BACK = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/wave_form_back.png");

    private static final int FRAMES = 4;
    private static final int FRAME_TIME = 2;

    private static final int BLURS = 5;

    public RenderEntityWaveForm(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityWaveForm e, double x, double y, double z, float entityYaw, float partialTicks) {
        calculateFrame(partialTicks, FRAME_TIME, FRAMES);

        ITessellator tessellator = TessellatorVertexBuffer.getInstance();

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.translate(x, y, z);

        //get caster
        EntityPlayer caster = e.getThrower();
        if(caster == null) {
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
            return;
        }

        //rotate in the direction of the caster' view
        Vec3d look = caster.getLook(partialTicks);
        double yaw = Math.toDegrees(Math.atan2(look.zCoord, look.xCoord));
        GlStateManager.rotate((float) -yaw, 0, 1, 0);

        //gl settings
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.05F);

        int width = 20;

        float minV = 16*(getFrame()) * (1.0F/FRAMES);
        float maxV = 16*(getFrame() + 1) * (1.0F/FRAMES);


        for(int blur = 0; blur < BLURS; blur++) {
            GlStateManager.color(255, 255, 255, 255 - (150*blur/BLURS));
            float scale = 1.0F - (blur * 0.15F);


            //render front
            tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
            tessellator.bindTexture(getEntityTexture(e));
            tessellator.setColorRGBA(1, 1, 1, 1 - ((3.0F*blur)/(5.0F*BLURS)));

            //left
            tessellator.addScaledVertexWithUV(0, 0, 0, 16, maxV);
            tessellator.addScaledVertexWithUV(0, 16*scale, 0, 16, minV);
            tessellator.addScaledVertexWithUV(-4*scale, 16*scale, width*scale, 0, minV);
            tessellator.addScaledVertexWithUV(-4*scale, 0, width*scale, 0, maxV);

            //right
            tessellator.addScaledVertexWithUV(0, 0, 0, 16, maxV);
            tessellator.addScaledVertexWithUV(-4*scale, 0, -width*scale, 0, maxV);
            tessellator.addScaledVertexWithUV(-4*scale, 16*scale, -width*scale, 0, minV);
            tessellator.addScaledVertexWithUV(0, 16*scale, 0, 16, minV);

            tessellator.draw();


            //render back
            tessellator.startDrawingQuads(DefaultVertexFormats.POSITION_TEX);
            tessellator.bindTexture(TEXTURE_BACK);

            //left
            tessellator.addScaledVertexWithUV(0, 0, 0, 16, maxV);
            tessellator.addScaledVertexWithUV(-4*scale, 0, width*scale, 0, maxV);
            tessellator.addScaledVertexWithUV(-4*scale, 16*scale, width*scale, 0, minV);
            tessellator.addScaledVertexWithUV(0, 16*scale, 0, 16, minV);

            //right
            tessellator.addScaledVertexWithUV(0, 0, 0, 16, maxV);
            tessellator.addScaledVertexWithUV(0, 16*scale, 0, 16, minV);
            tessellator.addScaledVertexWithUV(-4*scale, 16*scale, -width*scale, 0, minV);
            tessellator.addScaledVertexWithUV(-4*scale, 0, -width*scale, 0, maxV);

            tessellator.draw();

            GlStateManager.translate(0.2, 0, 0);
        }

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWaveForm entity) {
        return TEXTURE;
    }
}
