package com.infinityraider.elementalinvocations.registry;

import com.infinityraider.elementalinvocations.potion.PotionLivingArmor;
import com.infinityraider.elementalinvocations.potion.PotionConfusion;
import com.infinityraider.elementalinvocations.potion.PotionFrostArmor;
import com.infinityraider.elementalinvocations.potion.PotionWraithForm;
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
    }

    public final Potion POTION_CONFUSION;
    public final Potion POTION_WRAITH_FORM;
    public final Potion POTION_LIVING_ARMOR;
    public final Potion POTION_FROST_ARMOR;
}
