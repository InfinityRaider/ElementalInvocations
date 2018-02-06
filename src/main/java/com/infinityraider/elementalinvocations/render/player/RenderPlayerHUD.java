package com.infinityraider.elementalinvocations.render.player;

import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.api.souls.ISoulCollection;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.elementalinvocations.utility.debug.DebugModeRenderInstability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderPlayerHUD {
    private static final RenderPlayerHUD INSTANCE = new RenderPlayerHUD();

    public static RenderPlayerHUD getInstance() {
        return INSTANCE;
    }

    private static final ResourceLocation SOUL_TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/gui/counter_soul.png");



    private RenderPlayerHUD() {}

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onRenderScreen(RenderGameOverlayEvent.Post event) {
        if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
            ScaledResolution resolution = event.getResolution();
            if(event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS && DebugModeRenderInstability.doRender()) {
                this.renderInstability(resolution);
            }
            if(event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
                this.renderCharges(resolution);
                this.renderSouls(resolution);
            }
        }
    }

    private void renderCharges(ScaledResolution resolution) {
        RenderPlayerCharges.getInstance().renderChargesHUD(resolution);
    }

    private void renderSouls(ScaledResolution resolution) {
        ISoulCollection collection = CapabilityPlayerSoulCollection.getSoulCollection(ElementalInvocations.proxy.getClientPlayer());
        if(collection != null && collection.getSoulCount() > 0) {
            Minecraft.getMinecraft().renderEngine.bindTexture(SOUL_TEXTURE);
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer buffer = tessellator.getBuffer();

            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();

            int x0 = resolution.getScaledWidth()/2 + 10;
            int y0 = resolution.getScaledHeight() - 50;
            int total = collection.getSoulCount();
            int index = 0;

            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

            while(total > 0) {
                int amount = Math.min(total, 10);
                for(int i = 0; i < amount; i ++) {

                    int xMin = x0 + i*8;
                    int xMax = xMin + 9;
                    int yMin = y0 - index*8;
                    int yMax = yMin + 9;

                    buffer.pos(xMin, yMin, 0).tex(0, 0).endVertex();
                    buffer.pos(xMin, yMax, 0).tex(0, 1).endVertex();
                    buffer.pos(xMax, yMax, 0).tex(1, 1).endVertex();
                    buffer.pos(xMax, yMin, 0).tex(1, 0).endVertex();
                }
                index = index + 1;
                total = total - amount;
            }
            tessellator.draw();

            GlStateManager.popAttrib();
            GlStateManager.popMatrix();

            Minecraft.getMinecraft().renderEngine.bindTexture(Gui.ICONS);
        }
    }

    private void renderInstability(ScaledResolution resolution) {
        int x1 = resolution.getScaledWidth();
        int y1 = resolution.getScaledHeight();

        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(((double) x1)/2, ((double) y1)/2, 0);
        GlStateManager.rotate(180, 1, 0, 0);

        RenderInstability.getInstance().renderInstability(x1/3, y1/3, 0, 0.8F);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

        Minecraft.getMinecraft().renderEngine.bindTexture(Gui.ICONS);
    }
}
