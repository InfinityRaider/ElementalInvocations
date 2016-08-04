package com.teaminfinity.elementalinvocations.reference;

import com.teaminfinity.elementalinvocations.api.IPlayerMagicProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class Capabilities {
    @CapabilityInject(IPlayerMagicProperties.class)
    public static Capability<IPlayerMagicProperties> PLAYER_MAGIC_PROPERTIES = null;
}
