package com.teaminfinity.elementalinvocations.handler;

import com.teaminfinity.elementalinvocations.api.spells.ISpell;
import com.teaminfinity.elementalinvocations.api.spells.ISpellEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

public class SpellCastingHandler {
    private static final SpellCastingHandler INSTANCE = new SpellCastingHandler();

    public static SpellCastingHandler getInstance() {
        return INSTANCE;
    }
    /**  One-fire channeled effects which can not be active at the same time */
    private Map<UUID, List<ChannelProgress>> activeChanneledEffects;
    /** Continuous effects which can be effective while not channelling */
    private Map<UUID, List<ISpellEffect>> lingeringEffects;

    private SpellCastingHandler() {
        this.activeChanneledEffects = new HashMap<>();
        this.lingeringEffects = new HashMap<>();
    }

    public void castSpell(EntityPlayer caster, ISpell spell, int[] potencies) {
        if(!caster.getEntityWorld().isRemote) {
            if (!lingeringEffects.containsKey(caster.getUniqueID())) {
                lingeringEffects.put(caster.getUniqueID(), new ArrayList<>());
            }
            if (!activeChanneledEffects.containsKey(caster.getUniqueID())) {
                List<ChannelProgress> channelProgressList = new ArrayList<>();
                List<ISpellEffect> lingerEffectList = lingeringEffects.get(caster.getUniqueID());
                for (ISpellEffect effect : spell.getEffects()) {
                    channelProgressList.add(new ChannelProgress(effect, potencies));
                    if (effect.isLingeringEffect() && !lingerEffectList.contains(effect)) {
                        lingerEffectList.add(effect);
                    }
                }
                activeChanneledEffects.put(caster.getUniqueID(), channelProgressList);
            }
        }
    }

    public void onSpellAction(EntityPlayer caster) {
        if(lingeringEffects.containsKey(caster.getUniqueID())) {
            Iterator<ISpellEffect> iterator = lingeringEffects.get(caster.getUniqueID()).iterator();
            while(iterator.hasNext()) {
                ISpellEffect effect = iterator.next();
                if(effect.spellContextAction(caster)) {
                    iterator.remove();
                }
            }
        }
    }

    public void stopChanneling(EntityPlayer player) {
        if(activeChanneledEffects.containsKey(player.getUniqueID())) {
            activeChanneledEffects.remove(player.getUniqueID());
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.side == Side.SERVER && event.phase == TickEvent.Phase.END) {
            if(activeChanneledEffects.containsKey(event.player.getUniqueID())) {
                List<ChannelProgress> list = activeChanneledEffects.get(event.player.getUniqueID());
                Iterator<ChannelProgress> iterator = list.iterator();
                while(iterator.hasNext()) {
                    ChannelProgress progress = iterator.next();
                    if(!progress.update(event.player)) {
                        iterator.remove();
                    }
                }
                if(list.isEmpty()) {
                    activeChanneledEffects.remove(event.player.getUniqueID());
                }
            }
            if(lingeringEffects.containsKey(event.player.getUniqueID())) {
                Iterator<ISpellEffect> iterator = lingeringEffects.get(event.player.getUniqueID()).iterator();
                while(iterator.hasNext()) {
                    ISpellEffect effect = iterator.next();
                    if(effect.lingerUpdate(event.player)) {
                        iterator.remove();
                    }
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
