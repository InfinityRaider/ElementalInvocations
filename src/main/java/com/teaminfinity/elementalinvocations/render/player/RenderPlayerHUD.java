package com.teaminfinity.elementalinvocations.render.player;

import com.teaminfinity.elementalinvocations.ElementalInvocations;
import com.teaminfinity.elementalinvocations.api.souls.ISoulCollection;
import com.teaminfinity.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import com.teaminfinity.elementalinvocations.reference.Constants;
import com.teaminfinity.elementalinvocations.reference.Reference;
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
    public void onRenderScreen(RenderGameOverlayEvent.Pre event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
            ISoulCollection collection = CapabilityPlayerSoulCollection.getSoulCollection(ElementalInvocations.proxy.getClientPlayer());
            if(collection != null && collection.getSoulCount() > 0) {
                Minecraft.getMinecraft().renderEngine.bindTexture(SOUL_TEXTURE);
                Tessellator tessellator = Tessellator.getInstance();
                VertexBuffer buffer = tessellator.getBuffer();
                ScaledResolution resolution = event.getResolution();

                GlStateManager.pushMatrix();
                GlStateManager.pushAttrib();

                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                int x0 = resolution.getScaledWidth()/2 + 10;
                int y0 = resolution.getScaledHeight() - 50;
                float u = Constants.UNIT;
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

    }

}
