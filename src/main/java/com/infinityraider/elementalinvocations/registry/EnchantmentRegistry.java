package com.infinityraider.elementalinvocations.registry;

import com.infinityraider.elementalinvocations.enchantment.EnchantmentAugmentWand;
import net.minecraft.enchantment.Enchantment;
public class EnchantmentRegistry {
    private static final EnchantmentRegistry INSTANCE = new EnchantmentRegistry();

    public static EnchantmentRegistry getInstance() {
        return INSTANCE;
    }

    private EnchantmentRegistry() {
        this.ENCHANTMENT_AUGMENT_WAND = new EnchantmentAugmentWand();
    }

    public final Enchantment ENCHANTMENT_AUGMENT_WAND;
}