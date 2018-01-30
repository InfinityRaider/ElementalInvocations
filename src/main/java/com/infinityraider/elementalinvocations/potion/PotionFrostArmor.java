package com.infinityraider.elementalinvocations.potion;

import com.infinityraider.elementalinvocations.reference.Reference;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class PotionFrostArmor extends PotionBase implements IDamageReductor, IPotionWithRenderOverlay {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/gui/potion_frost_armor.png");
    public static final ResourceLocation OVERLAY = new ResourceLocation(Reference.MOD_ID.toLowerCase(), "textures/entities/player/frost_armor.png");

    public PotionFrostArmor() {
        super(false, "frost_armor", 0x49EFF1);
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
        if (entity.onGround) {
            BlockPos pos = entity.getPosition();
            BlockPos below = pos.down();
            IBlockState state = entity.getEntityWorld().getBlockState(below);
            if(state.getBlock() == Blocks.LAVA || state.getBlock() == Blocks.FLOWING_LAVA) {
                entity.getEntityWorld().setBlockState(below, Blocks.OBSIDIAN.getDefaultState(), 3);
            } else if(state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER) {
                entity.getEntityWorld().setBlockState(below, Blocks.ICE.getDefaultState(), 3);
            } else if(state.getBlock() == Blocks.DIRT) {
                IBlockState newState = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.SNOWY, true);
                entity.getEntityWorld().setBlockState(below, newState, 3);
                entity.getEntityWorld().notifyBlockUpdate(below, state, newState, 3);
            } else if(state.getBlock() == Blocks.GRASS) {
                IBlockState newState = Blocks.GRASS.getDefaultState().withProperty(BlockGrass.SNOWY, true);
                entity.getEntityWorld().setBlockState(below, newState, 3);
                entity.getEntityWorld().notifyBlockUpdate(below, state, newState, 3);
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

    @Override
    public ResourceLocation getOverlayTexture() {
        return OVERLAY;
    }
}
