package com.infinityraider.elementalinvocations.api;

import com.infinityraider.elementalinvocations.api.souls.ISoulCollection;
import com.infinityraider.elementalinvocations.api.spells.ISpellBuilder;
import com.infinityraider.elementalinvocations.api.spells.ISpellRegistry;
import net.minecraftforge.common.capabilities.Capability;

public interface IElementalInvocationsAPI {
    Capability<? extends IPlayerMagicProperties> magicPropertiesCapability();

    Capability<? extends ISoulCollection> soulCollectionCapability();

    ISpellRegistry getSpellRegistry();

    ISpellBuilder getNewSpellBuilder();

    IMagicDamageHandler getDamageHandler();
}