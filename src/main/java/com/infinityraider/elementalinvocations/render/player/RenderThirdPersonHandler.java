package com.infinityraider.elementalinvocations.render.player;

import com.infinityraider.elementalinvocations.api.*;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerMagicProperties;
import com.infinityraider.elementalinvocations.magic.MagicChargeConfiguration;
import com.infinityraider.elementalinvocations.magic.spell.BeamHandler;
import com.infinityraider.elementalinvocations.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class RenderThirdPersonHandler extends RenderUtil {
    private static final RenderThirdPersonHandler INSTANCE = new RenderThirdPersonHandler();

    public static RenderThirdPersonHandler getInstance() {
        return INSTANCE;
    }

    private final MagicChargeRenderer renderer;

    private RenderThirdPersonHandler() {
        this.renderer = MagicChargeRenderer.getInstance();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    public void onRenderPlayer(RenderPlayerEvent.Post event) {
        IPlayerMagicProperties properties = CapabilityPlayerMagicProperties.getMagicProperties(event.getEntityPlayer());
        if(properties == null) {
            return;
        }
        IChargeConfiguration charges = properties.getChargeConfiguration();

        GlStateManager.pushMatrix();

        GlStateManager.translate(event.getX(), event.getY(), event.getZ());
        //charges
        this.renderer.renderChargesThirdPerson(charges.getCharges());
        //magic effect
        if(charges instanceof MagicChargeConfiguration) {
            ((MagicChargeConfiguration) charges).getEffectTimers().forEach(timer -> {
                switch(timer.getType()) {
                    case INVOKE:
                        this.renderer.renderInvokeThirdPerson(timer.getCharges(), timer.getFrame(), timer.getTotal(), event.getPartialRenderTick());
                        return;
                    case FADE:
                        this.renderer.renderFadeThirdPerson(timer.getCharges(), timer.getFrame(), timer.getTotal(), event.getPartialRenderTick());
                        return;
                    case FIZZLE:
                        this.renderer.renderFizzleThirdPerson(timer.getCharges(), timer.getFrame(), timer.getTotal(), event.getPartialRenderTick());
                }
            });
        }
        //beam
        BeamHandler.getInstance().getMagicBeam(event.getEntityPlayer()).ifPresent(beam -> BeamRenderer.getInstance().renderBeamThirdPerson(beam, event.getPartialRenderTick()));

        GlStateManager.popMatrix();
    }
}
