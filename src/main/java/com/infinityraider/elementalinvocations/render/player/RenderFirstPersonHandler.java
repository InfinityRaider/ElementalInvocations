package com.infinityraider.elementalinvocations.render.player;

import com.infinityraider.elementalinvocations.ElementalInvocations;
import com.infinityraider.elementalinvocations.api.IChargeConfiguration;
import com.infinityraider.elementalinvocations.api.IPlayerMagicProperties;
import com.infinityraider.elementalinvocations.api.souls.ISoulCollection;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import com.infinityraider.elementalinvocations.magic.MagicChargeConfiguration;
import com.infinityraider.elementalinvocations.magic.spell.BeamHandler;
import com.infinityraider.elementalinvocations.reference.Reference;
import com.infinityraider.elementalinvocations.utility.debug.DebugModeRenderInstability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderFirstPersonHandler {
    private static final RenderFirstPersonHandler INSTANCE = new RenderFirstPersonHandler();

    public static RenderFirstPersonHandler getInstance() {
        return INSTANCE;
    }

    private static final ResourceLocation SOUL_TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/gui/counter_soul.png");

    private final MagicChargeRenderer renderer;

    private RenderFirstPersonHandler() {
        this.renderer = MagicChargeRenderer.getInstance();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onRenderScreen(RenderGameOverlayEvent.Post event) {
        if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
            ScaledResolution resolution = event.getResolution();
            if(event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS && DebugModeRenderInstability.doRender()) {
                this.renderInstability(resolution);
            }
            if(event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
                this.renderCharges(event);
                this.renderSouls(resolution);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onRenderHand(RenderHandEvent event) {
        if(Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) {
            return;
        }
        EntityPlayer player = ElementalInvocations.proxy.getClientPlayer();
        if(player == null) {
            return;
        }
        BeamHandler.getInstance().getMagicBeam(player).ifPresent(beam -> BeamRenderer.getInstance().renderBeamFirstPerson(beam, event.getPartialTicks()));
    }

    private void renderCharges(RenderGameOverlayEvent event) {
        IPlayerMagicProperties props = CapabilityPlayerMagicProperties.getMagicProperties(ElementalInvocations.proxy.getClientPlayer());
        if(props == null) {
            return;
        }
        IChargeConfiguration charges = props.getChargeConfiguration();
        MagicChargeRenderer.getInstance().renderChargesFirstPerson(charges.getCharges(),  event.getResolution());
        if(charges instanceof MagicChargeConfiguration) {
            ((MagicChargeConfiguration) charges).getEffectTimers().forEach(timer -> {
                switch(timer.getType()) {
                    case INVOKE:
                        this.renderer.renderInvokeFirstPerson(timer.getCharges(), timer.getPotencies(), timer.getFrame(), timer.getTotal(), event.getPartialTicks(), event.getResolution());
                        return;
                    case FADE:
                        this.renderer.renderFadeFirstPerson(timer.getCharges(), timer.getPotencies(), timer.getFrame(), timer.getTotal(), event.getPartialTicks(), event.getResolution());
                        return;
                    case FIZZLE:
                        this.renderer.renderFizzleFirstPerson(timer.getCharges(), timer.getPotencies(), timer.getFrame(), timer.getTotal(), event.getPartialTicks(), event.getResolution());
                }
            });
        }
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
        //fetch player properties
        EntityPlayer player = ElementalInvocations.proxy.getClientPlayer();
        if(player == null) {
            return;
        }

        int x1 = resolution.getScaledWidth();
        int y1 = resolution.getScaledHeight();

        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(((double) x1)/2, ((double) y1)/2, 0);
        GlStateManager.rotate(180, 1, 0, 0);

        InstabilityRenderer.getInstance().renderInstability(CapabilityPlayerMagicProperties.getMagicProperties(player), x1/3, y1/3, 0, 0.8F);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

        Minecraft.getMinecraft().renderEngine.bindTexture(Gui.ICONS);
    }
}
