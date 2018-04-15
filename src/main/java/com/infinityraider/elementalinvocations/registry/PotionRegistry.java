package com.infinityraider.elementalinvocations.registry;

import com.infinityraider.elementalinvocations.potion.*;
import net.minecraft.potion.Potion;

public class PotionRegistry {
    private static final PotionRegistry INSTANCE = new PotionRegistry();

    public static PotionRegistry getInstance() {
        return INSTANCE;
    }

    private PotionRegistry() {
        this.POTION_CONFUSION = new PotionConfusion();
        this.POTION_WRAITH_FORM = new PotionWraithForm();
        this.POTION_LIVING_ARMOR = new PotionLivingArmor();
        this.POTION_FROST_ARMOR = new PotionFrostArmor();
        this.POTION_HEX = new PotionHex();
    }

    public final Potion POTION_CONFUSION;
    public final Potion POTION_WRAITH_FORM;
    public final Potion POTION_LIVING_ARMOR;
    public final Potion POTION_FROST_ARMOR;
    public final Potion POTION_HEX;
}
