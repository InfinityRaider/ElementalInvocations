package com.infinityraider.elementalinvocations.api;

import com.infinityraider.elementalinvocations.api.souls.ISoulCollection;
import com.infinityraider.elementalinvocations.api.spells.ISpellBuilder;
import com.infinityraider.elementalinvocations.api.spells.ISpellRegistry;
import net.minecraftforge.common.capabilities.Capability;

import java.util.Optional;

public class ElementalInvocationsAPI implements IElementalInvocationsAPI {
    private static IElementalInvocationsAPI API;

    public static Optional<IElementalInvocationsAPI> getApi() {
        return Optional.ofNullable(API);
    }

    @Override
    public Capability<? extends IPlayerMagicProperties> magicPropertiesCapability() {
        return API.magicPropertiesCapability();
    }

    @Override
    public Capability<? extends ISoulCollection> soulCollectionCapability() {
        return API.soulCollectionCapability();
    }

    @Override
    public ISpellRegistry getSpellRegistry() {
        return API.getSpellRegistry();
    }

    @Override
    public ISpellBuilder getNewSpellBuilder() {
        return API.getNewSpellBuilder();
    }

    @Override
    public IMagicDamageHandler getDamageHandler() {
        return API.getDamageHandler();
    }
}