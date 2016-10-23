package com.teaminfinity.elementalinvocations.render.player;

import com.infinityraider.infinitylib.modules.specialpotioneffect.CapabilityPotionTracker;
import com.infinityraider.infinitylib.modules.specialpotioneffect.PotionTracker;
import com.infinityraider.infinitylib.utility.LogHelper;
import com.teaminfinity.elementalinvocations.potion.IPotionWithRenderOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class RenderLayerPotionEffect implements LayerRenderer<EntityPlayer> {
    private final RenderPlayer renderer;

    private RenderLayerPotionEffect(RenderPlayer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw, float headPitch, float scale) {
        PotionTracker tracker = CapabilityPotionTracker.getPotionTracker(player);
        if(tracker == null) {
            return;
        }
        List<IPotionWithRenderOverlay> specialPotions = tracker.getActivePotions().stream()
                .filter(p -> p instanceof IPotionWithRenderOverlay)
                .map(p -> (IPotionWithRenderOverlay) p)
                .collect(Collectors.toList());

        if(specialPotions.size() > 0) {
            this.renderPotionEffects(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, headYaw, headPitch, scale, specialPotions);
        }
    }

    protected void renderPotionEffects(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw, float headPitch, float scale, List<IPotionWithRenderOverlay> effects) {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.scale(1.1, 1.1, 1.1);

        ModelBase model = this.renderer.getMainModel();
        model.setModelAttributes(this.renderer.getMainModel());
        model.setLivingAnimations(player, limbSwing, limbSwingAmount, partialTicks);

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();

        float angle = (float) (360 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
        angle = (3 * angle) % 360;
        int index = 0;

        for(IPotionWithRenderOverlay effect : effects) {
            Minecraft.getMinecraft().renderEngine.bindTexture(effect.getOverlayTexture());

            double alpha = 0.3 + 0.5 * (Math.cos(Math.toRadians(angle + index*360/effects.size())) + 1)/2;
            GlStateManager.color(1, 1, 1, (float) alpha);

            model.render(player, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale);

            GlStateManager.scale(1.001, 1.001, 1.001);
            index++;
        }
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    @SuppressWarnings("unchecked")
    public static void init() {
        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        for(Field field : renderManager.getClass().getDeclaredFields()) {
            if(field.getType() != Map.class) {
                continue;
            }
            field.setAccessible(true);
            Object mapObject = null;
            try {
                mapObject = field.get(renderManager);
            } catch (IllegalAccessException e) {
                LogHelper.printStackTrace(e);
            }
            if(mapObject == null || mapObject == renderManager.entityRenderMap) {
                continue;
            }
            Map<String, RenderPlayer> renderPlayerMap = (Map<String, RenderPlayer>) mapObject;
            for(RenderPlayer renderer : renderPlayerMap.values()) {
                renderer.addLayer(new RenderLayerPotionEffect(renderer));
            }
        }
    }
}
