package com.infinityraider.elementalinvocations.enchantment;

import com.infinityraider.elementalinvocations.config.ModConfiguration;
import com.infinityraider.elementalinvocations.item.ItemWand;
import com.infinityraider.elementalinvocations.reference.Constants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class EnchantmentAugmentWand extends Enchantment {

    public EnchantmentAugmentWand() {
        super(Rarity.VERY_RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND});
        this.setName("enchantment.augment_wand");
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 0;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return ModConfiguration.getInstance().getLevelsForTierLevelup()* Constants.CORE_TIERS;
    }

    @Nullable
    @Override
    public List<ItemStack> getEntityEquipment(EntityLivingBase entity) {
        return Collections.emptyList();
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return false;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() != null
                && stack.getItem() instanceof ItemWand
                && stack.getItem().isEnchantable(stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return this.canApply(stack);
    }

    @Override
    public boolean isAllowedOnBooks() {
        return false;
    }
}