package com.infinityraider.elementalinvocations.handler;

import com.infinityraider.elementalinvocations.potion.IDamageReductor;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DamageReductorHandler {
    private static final DamageReductorHandler INSTANCE = new DamageReductorHandler();

    public static DamageReductorHandler getInstance() {
        return INSTANCE;
    }

    private DamageReductorHandler() {

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onEntityHurtEvent(LivingHurtEvent event) {
        if(!event.getEntity().worldObj.isRemote) {
            for(PotionEffect effect : event.getEntityLiving().getActivePotionEffects()) {
                if(effect.getPotion() instanceof IDamageReductor) {
                    float reduction = ((IDamageReductor) effect.getPotion()).getDamageReduction(event.getEntityLiving(), event.getSource(), event.getAmount(), effect);
                    reduction = Math.max(0, Math.min(1, reduction));
                    event.setAmount(event.getAmount()*(1 - reduction));
                    if(event.getAmount() <= 0) {
                        event.setResult(Event.Result.DENY);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
