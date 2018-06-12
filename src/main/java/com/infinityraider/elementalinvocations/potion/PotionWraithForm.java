package com.infinityraider.elementalinvocations.potion;

import com.infinityraider.elementalinvocations.api.souls.ISoulCollection;
import com.infinityraider.elementalinvocations.capability.CapabilityPlayerSoulCollection;
import com.infinityraider.elementalinvocations.reference.Reference;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class PotionWraithForm extends PotionBase {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/potion_wraith.png");

    public PotionWraithForm() {
        super(false, "wraith_form", 0);
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
        if(!(entity instanceof EntityPlayer)) {
            entity.setDead();
        }
        EntityPlayer player = (EntityPlayer) entity;
        if(player.getHealth() <= 1.0F) {
            ISoulCollection collection = CapabilityPlayerSoulCollection.getSoulCollection(player);
            if(collection != null && collection.getSoulCount() > 0) {
                collection.removeSoul();
                player.setHealth(amplification);
            }
        }
        player.attackEntityFrom(new DamageSourceWraithWither(), 1);
    }

    public static class DamageSourceWraithWither extends DamageSource {
        public DamageSourceWraithWither() {
            super("wither");
        }
    }
}
