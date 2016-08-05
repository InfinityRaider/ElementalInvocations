package com.teaminfinity.elementalinvocations.reference;

import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import com.teaminfinity.elementalinvocations.api.souls.ISoulCollection;

public class Capabilities {
    @CapabilityInject(IPlayerMagicProperties.class)
    public static Capability<IPlayerMagicProperties> PLAYER_MAGIC_PROPERTIES = null;

    @CapabilityInject(ISoulCollection.class)
    public static Capability<ISoulCollection> PLAYER_SOUL_COLLECTION = null;
}
