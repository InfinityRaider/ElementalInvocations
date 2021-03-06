package com.infinityraider.elementalinvocations.potion;

import com.infinityraider.elementalinvocations.reference.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class PotionLivingArmor extends PotionBase implements IDamageReductor, IPotionWithRenderOverlay {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/potion_living_armor.png");
    public static final ResourceLocation OVERLAY = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/player/living_armor.png");

    public PotionLivingArmor() {
        super(false, "living_armor", 0x047040);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplification) {
        if (entity.getHealth() < entity.getMaxHealth()) {
            entity.heal(1.0F);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public float getDamageReduction(EntityLivingBase entity, DamageSource source, float amount, PotionEffect effect) {
        int potency = effect.getAmplifier();
        return ((float) potency)/(3 * 10.0F);
    }

    @Override
    public ResourceLocation getOverlayTexture() {
        return OVERLAY;
    }
}
