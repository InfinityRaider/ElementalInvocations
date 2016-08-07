package com.teaminfinity.elementalinvocations.potion;

import com.teaminfinity.elementalinvocations.handler.ConfigurationHandler;
import com.teaminfinity.elementalinvocations.reference.Reference;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;

public class PotionRegistry {
    private static final PotionRegistry INSTANCE = new PotionRegistry();

    public static PotionRegistry getInstance() {
        return INSTANCE;
    }

    private final RegistryNamespaced<ResourceLocation, Potion> potionRegistry;
    private int lastId = -1;

    private PotionRegistry() {
        this.potionRegistry = Potion.REGISTRY;
        this.POTION_CONFUSION = this.registerPotion(new PotionConfusion());
        this.POTION_WRAITH_FORM = this.registerPotion(new PotionWraithForm());
        this.POTION_LIVING_ARMOR = this.registerPotion(new PotionLivingArmor());
    }

    public final Potion POTION_CONFUSION;
    public final Potion POTION_WRAITH_FORM;
    public final Potion POTION_LIVING_ARMOR;

    private Potion registerPotion(Potion potion) {
        String name = potion.getName();
        int id = ConfigurationHandler.getInstance().getPotionEffectId(name, getNextId());
        this.potionRegistry.register(id, new ResourceLocation(Reference.MOD_ID, name), potion);
        return potion;
    }

    private int getNextId() {
        int id = lastId;
        boolean flag = false;
        while(!flag) {
            id = id + 1;
            Potion potion = potionRegistry.getObjectById(id);
            if(potion == null) {
                flag = true;
            }
        }
        lastId = id;
        return id;
    }
}
