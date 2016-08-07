package com.teaminfinity.elementalinvocations.potion;

import com.teaminfinity.elementalinvocations.reference.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class PotionFrostArmor extends PotionBase implements IDamageReductor {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/potion_frost_armor.png");

    protected PotionFrostArmor() {
        super(false, "frost_armor", 0x49EFF1);
    }
    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplification) {
        if (entity.onGround) {
            BlockPos pos = entity.getPosition();
            IBlockState state = entity.getEntityWorld().getBlockState(pos);
            while (state.getMaterial() == Material.AIR) {
                pos = pos.down();
                state = entity.getEntityWorld().getBlockState(pos);
            }
        }
    }

    @Override
    public float getDamageReduction(EntityLivingBase entity, DamageSource source, float amount, PotionEffect effect) {
        int potency = effect.getAmplifier();
        Entity attacker = source.getSourceOfDamage();
        if(attacker != null && (attacker instanceof EntityLivingBase) && attacker != entity) {
            ((EntityLivingBase) attacker).addPotionEffect(new PotionEffect(Potion.getPotionById(2), 60));
        }
        return ((float) potency)/(3 * 10.0F);
    }
}
