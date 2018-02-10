package com.infinityraider.elementalinvocations.handler;

import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.spells.ISpell;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import net.minecraft.entity.player.EntityPlayer;

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

    public void castSpell(EntityPlayer caster, ISpell spell, IPotencyMap potencies) {
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
            activeChanneledEffects.get(player.getUniqueID()).forEach(x -> x.stopChannelling(player));
            activeChanneledEffects.remove(player.getUniqueID());
        }
    }

    public void onPlayerTick(EntityPlayer player) {
        if (activeChanneledEffects.containsKey(player.getUniqueID())) {
            List<ChannelProgress> list = activeChanneledEffects.get(player.getUniqueID());
            Iterator<ChannelProgress> iterator = list.iterator();
            while (iterator.hasNext()) {
                ChannelProgress progress = iterator.next();
                if (!progress.update(player)) {
                    iterator.remove();
                }
            }
            if (list.isEmpty()) {
                activeChanneledEffects.remove(player.getUniqueID());
            }
        }
        if (lingeringEffects.containsKey(player.getUniqueID())) {
            Iterator<ISpellEffect> iterator = lingeringEffects.get(player.getUniqueID()).iterator();
            while (iterator.hasNext()) {
                ISpellEffect effect = iterator.next();
                if (effect.lingerUpdate(player)) {
                    iterator.remove();
                }
            }
        }
    }

    private static class ChannelProgress {
        private final ISpellEffect effect;
        private final IPotencyMap potency;
        private int channelTicks;

        private ChannelProgress(ISpellEffect spellEffect, IPotencyMap potency) {
            this.effect = spellEffect;
            this.potency = potency;
            this.channelTicks = 0;
        }

        private boolean update(EntityPlayer caster) {
            boolean flag = effect.apply(caster, potency, channelTicks);
            channelTicks++;
            return flag;
        }

        private void stopChannelling(EntityPlayer player) {
            effect.onPlayerStopChanneling(player, potency, channelTicks);
        }
    }
}
