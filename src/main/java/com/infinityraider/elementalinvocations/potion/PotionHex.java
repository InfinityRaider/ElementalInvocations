package com.infinityraider.elementalinvocations.potion;

import com.infinityraider.elementalinvocations.handler.HexHandler;
import com.infinityraider.elementalinvocations.reference.Reference;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

public class PotionHex extends PotionBase {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/gui/potion_hex.png");

    public PotionHex() {
        super(true, "hex", new Color(0, 0, 0).getRGB());
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplification) {
        HexHandler.getInstance().onHexedTick(entity);
        if(entity instanceof EntityLiving) {
            ((EntityLiving) entity).setAttackTarget(null);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasStatusIcon() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRenderHUD(PotionEffect effect) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRender(PotionEffect effect) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) {
        mc.getTextureManager().bindTexture(TEXTURE);
        net.minecraft.client.renderer.GlStateManager.color(1, 1, 1, 1);
        net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0F, 0F, 18, 18, 18, 18);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc, float alpha) {
        mc.getTextureManager().bindTexture(TEXTURE);
        net.minecraft.client.renderer.GlStateManager.color(1, 1, 1, alpha);
        net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0F, 0F, 18, 18, 18, 18);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}