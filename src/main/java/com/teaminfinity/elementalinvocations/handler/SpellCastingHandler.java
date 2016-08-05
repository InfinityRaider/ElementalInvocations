package com.teaminfinity.elementalinvocations.handler;

import com.teaminfinity.elementalinvocations.api.spells.ISpell;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;
import java.util.stream.Collectors;

public class SpellCastingHandler {
    private static final SpellCastingHandler INSTANCE = new SpellCastingHandler();

    public static SpellCastingHandler getInstance() {
        return INSTANCE;
    }

    private Map<UUID, List<ChannelProgress>> activeSpellEffects;

    private SpellCastingHandler() {
        activeSpellEffects = new HashMap<>();
    }

    public void castSpell(EntityPlayer caster, ISpell spell, int[] potencies) {
        if(!activeSpellEffects.containsKey(caster.getUniqueID())) {
            List<ChannelProgress> list = spell.getEffects().stream().map(effect -> new ChannelProgress(effect, potencies)).collect(Collectors.toList());
            activeSpellEffects.put(caster.getUniqueID(), list);
        }
    }

    public void stopChanneling(EntityPlayer player) {
        if(activeSpellEffects.containsKey(player.getUniqueID())) {
            activeSpellEffects.remove(player.getUniqueID());
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.side == Side.SERVER && event.phase == TickEvent.Phase.END) {
            if(activeSpellEffects.containsKey(event.player.getUniqueID())) {
                List<ChannelProgress> list = activeSpellEffects.get(event.player.getUniqueID());
                Iterator<ChannelProgress> iterator = list.iterator();
                while(iterator.hasNext()) {
                    ChannelProgress progress = iterator.next();
                    if(!progress.update(event.player)) {
                        iterator.remove();
                    }
                }
                if(list.isEmpty()) {
                    activeSpellEffects.remove(event.player.getUniqueID());
                }
            }
        }
    }

    private static class ChannelProgress {
        private final ISpellEffect effect;
        private final int[] potency;
        private int channelTicks;

        private ChannelProgress(ISpellEffect spellEffect, int[] potency) {
            this.effect = spellEffect;
            this.potency = potency;
            this.channelTicks = 0;
        }

        private boolean update(EntityPlayer caster) {
            boolean flag = effect.apply(caster, potency, channelTicks);
            channelTicks++;
            return flag;
        }
    }
}
