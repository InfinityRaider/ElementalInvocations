package com.infinityraider.elementalinvocations.magic.spell.earth;

import com.infinityraider.elementalinvocations.api.Element;
import com.infinityraider.elementalinvocations.api.IPotencyMap;
import com.infinityraider.elementalinvocations.api.spells.ISpellEffect;
import com.infinityraider.elementalinvocations.registry.PotionRegistry;
import com.infinityraider.elementalinvocations.registry.SoundRegistry;
import com.infinityraider.infinitylib.sound.ModSoundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;

public class EffectLivingArmor implements ISpellEffect {
    @Override
    public boolean apply(EntityPlayer caster, IPotencyMap potencies, int channelTick) {
        caster.addPotionEffect(new PotionEffect(PotionRegistry.getInstance().POTION_LIVING_ARMOR, 20*potencies.getPotency(Element.LIFE), potencies.getPotency(Element.EARTH)));
        ModSoundHandler.getInstance().playSoundAtEntityOnce(caster, SoundRegistry.getInstance().SOUND_LIVING_ARMOR, SoundCategory.PLAYERS);
        return false;
    }
}
