package com.infinityraider.elementalinvocations.render.entity;

import com.infinityraider.elementalinvocations.entity.EntityMeteor;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.elementalinvocations.render.model.ModelMeteor;
import com.infinityraider.infinitylib.reference.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntityMeteor extends Render<EntityMeteor> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/meteor.png");
    private static final ModelMeteor MODEL = new ModelMeteor();

    public RenderEntityMeteor(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityMeteor e, double x, double y, double z, float entityYaw, float partialTicks) {
        Minecraft.getMinecraft().renderEngine.bindTexture(getEntityTexture(e));

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.translate(x, y, z);

        float newAngle = (float) (360 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);


        GlStateManager.rotate(newAngle, 1, 0, 0);
        GlStateManager.rotate(newAngle, 0, 1, 0);

        GlStateManager.translate(0, -5, 0);


        MODEL.render(e, 0, 0, 0, 0, 0, Constants.UNIT * (3 + e.getPotencyEarth()/3));

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();

        e.spawnFireParticles();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMeteor entity) {
        return TEXTURE;
    }
}
