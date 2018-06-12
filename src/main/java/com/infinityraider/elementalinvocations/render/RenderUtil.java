package com.infinityraider.elementalinvocations.render;

import com.infinityraider.elementalinvocations.config.ModConfiguration;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtil {

    /**
     * Used for debugging rendering stuff
     */
    @SuppressWarnings("unused")
    public void renderCoordinateSystemDebug() {
        if(ModConfiguration.getInstance().debug()) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            GlStateManager.pushAttrib();
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();

            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for(int i = 0; i <= 16; i++) {
                buffer.pos(((float) i) / 16.0F, 0, 0).color(255, 0, 0, 255).endVertex();
            }
            tessellator.draw();

            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for(int i = 0; i <= 16; i++) {
                buffer.pos(0, ((float) i) / 16.0F, 0).color(0, 255, 0, 255).endVertex();
            }
            tessellator.draw();

            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            for(int i = 0; i <= 16; i++) {
                buffer.pos(0, 0, ((float) i) / 16.0F).color(0, 0, 255, 255).endVertex();
            }
            tessellator.draw();

            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.popAttrib();
        }
    }
}
