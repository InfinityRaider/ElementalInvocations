package com.teaminfinity.elementalinvocations.potion;

import com.teaminfinity.elementalinvocations.reference.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class PotionWraithForm extends PotionBase {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/potionWraithForm.png");

    protected PotionWraithForm() {
        super(false, 0);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplification) {
    }


}
